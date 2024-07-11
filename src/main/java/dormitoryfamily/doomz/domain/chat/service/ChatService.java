package dormitoryfamily.doomz.domain.chat.service;

import dormitoryfamily.doomz.domain.chat.dto.response.ChatResponseDto;
import dormitoryfamily.doomz.domain.chat.dto.response.SearchChatListResponseDto;
import dormitoryfamily.doomz.domain.chat.dto.response.SearchChatResponseDto;
import dormitoryfamily.doomz.domain.chat.dto.response.ChatListResponseDto;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.exception.AlreadyChatRoomLeftException;
import dormitoryfamily.doomz.domain.chatRoom.exception.ChatRoomNotExistsException;
import dormitoryfamily.doomz.domain.chatRoom.exception.MemberNotInChatRoomException;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.chat.ChatMessage;
import dormitoryfamily.doomz.global.chat.exception.InvalidChatMessageException;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, ChatDto> redisTemplateMessage;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveChat(ChatMessage chatMessage) {
        ChatRoom chatRoom = getChatRoomByRoomUUID(chatMessage.getRoomUUID());
        Chat chat = ChatMessage.toEntity(chatMessage, chatRoom);
        chatRepository.save(chat);
        ChatDto chatDto = ChatDto.fromEntity(chat);

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));
        ZSetOperations<String, ChatDto> zSetOps = redisTemplateMessage.opsForZSet();
        zSetOps.add(chatMessage.getRoomUUID(), chatDto, chat.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
        redisTemplateMessage.expire(chatMessage.getRoomUUID(), 1, TimeUnit.DAYS);
    }

    private ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    public void clearChatIfNeed(LocalDateTime enteredAt, String roomUUID) {
        chatRepository.deleteByCreatedAtBefore(enteredAt);
        redisTemplateMessage.delete(roomUUID);
    }

    public ChatListResponseDto findAllChatHistory(PrincipalDetails principalDetails, Long roomId, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());

        validateChatRoomAccessAndStatus(chatRoom, loginMember, isSender);
        updateMemberStatusToIn(chatRoom, isSender);
        return getChatListResponse(chatRoom, loginMember, isSender, pageable);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    private void validateChatRoomAccessAndStatus(ChatRoom chatRoom, Member loginMember, boolean isSender) {
        if (!isSender && !chatRoom.getReceiver().getId().equals(loginMember.getId())) {
            throw new MemberNotInChatRoomException();
        }

        if ((isSender && chatRoom.getSenderEnteredAt() == null) || (!isSender && chatRoom.getReceiverEnteredAt() == null)) {
            throw new AlreadyChatRoomLeftException();
        }
    }

    private void updateMemberStatusToIn(ChatRoom chatRoom, boolean isSender) {
        if (isSender) {
            chatRoom.senderInChatRoom();
        } else {
            chatRoom.receiverInChatRoom();
        }
    }

    private ChatListResponseDto getChatListResponse(ChatRoom chatRoom, Member loginMember, boolean isSender, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        double startScore = calculateStartScore(chatRoom, isSender);
        double endScore = Double.POSITIVE_INFINITY;

        ZSetOperations<String, ChatDto> zSetOps = redisTemplateMessage.opsForZSet();
        Long sizeInRange = zSetOps.count(chatRoom.getRoomUUID(), startScore, endScore);

        long remainingCount = sizeInRange - ((long) pageSize * pageNumber);
        if (remainingCount < 0) {
            return ChatListResponseDto.from(pageNumber, true, chatRoom.getRoomUUID(), Collections.emptyList());
        }

        long offset = Math.max(sizeInRange - (long) (pageNumber + 1) * pageSize, 0);
        long count = Math.min(remainingCount, pageable.getPageSize());

        Set<ChatDto> chatSet = zSetOps.rangeByScore(chatRoom.getRoomUUID(), startScore, endScore, offset, count);

        List<ChatResponseDto> chatResponseDtos;
        if (chatSet != null && !chatSet.isEmpty()) {
            chatResponseDtos = getChatResponseDtoList(chatSet, chatRoom, loginMember);
            Collections.reverse(chatResponseDtos);
        } else {
            Slice<Chat> chatSlice = getChatListFromRDB(chatRoom, pageable, isSender);
            List<ChatDto> chatList = chatSlice.stream().map(ChatDto::fromEntity).collect(Collectors.toList());
            chatResponseDtos = getChatResponseDtoList(new HashSet<>(chatList), chatRoom, loginMember);
        }

        boolean isLast = chatResponseDtos.size() < pageSize;
        return ChatListResponseDto.from(pageNumber, isLast, chatRoom.getRoomUUID(), chatResponseDtos);
    }

    private double calculateStartScore(ChatRoom chatRoom, boolean isSender) {
        return isSender ? chatRoom.getSenderEnteredAt().toEpochSecond(ZoneOffset.UTC) :
                chatRoom.getReceiverEnteredAt().toEpochSecond(ZoneOffset.UTC);
    }

    private List<ChatResponseDto> getChatResponseDtoList(Set<ChatDto> chatSet, ChatRoom chatRoom, Member loginMember) {
        List<ChatDto> chatList = new ArrayList<>(chatSet);
        return chatList.stream()
                .map(chat -> {
                    Member chatMember = Objects.equals(chat.senderId(), chatRoom.getSender().getId()) ?
                            chatRoom.getSender() : chatRoom.getReceiver();
                    boolean isChatSender = chat.senderId().equals(loginMember.getId());
                    return ChatResponseDto.fromChatDto(chat, chatMember, isChatSender);
                })
                .collect(Collectors.toList());
    }

    private Slice<Chat> getChatListFromRDB(ChatRoom chatRoom, Pageable pageable, boolean isSender) {
        String roomUUID = chatRoom.getRoomUUID();

        List<Chat> dbChatList = chatRepository.findAllByChatRoomRoomUUID(roomUUID);
        ZSetOperations<String, ChatDto> zSetOps = redisTemplateMessage.opsForZSet();
        dbChatList.stream()
                .map(ChatDto::fromEntity)
                .forEach(chatDto -> {
                    zSetOps.add(roomUUID, chatDto, chatDto.sentTime().toEpochSecond(ZoneOffset.UTC));
                    redisTemplateMessage.expire(roomUUID, 1, TimeUnit.DAYS);
                });
        if (isSender) {
            return chatRepository.findByRoomUUIDAndCreatedAtAfter(roomUUID, chatRoom.getSenderEnteredAt(), pageable);
        } else {
            return chatRepository.findByRoomUUIDAndCreatedAtAfter(roomUUID, chatRoom.getReceiverEnteredAt(), pageable);
        }
    }

    public void validateChat(ChatMessage chatMessage) {
        Long senderId = chatMessage.getSenderId();
        ChatRoom chatRoom = findChatRoomByUUID(chatMessage.getRoomUUID());
        validateMemberInChatRoom(chatRoom, senderId);
        validateChatMessage(chatMessage);
    }

    private ChatRoom findChatRoomByUUID(String roomUUID) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByRoomUUID(roomUUID);
        return optionalChatRoom.orElseThrow(() -> new InvalidChatMessageException("존재하지 않는 채팅방입니다."));
    }

    private void validateMemberInChatRoom(ChatRoom chatRoom, Long senderId) {
        boolean isChatRoomMember = chatRoom.getSender().getId().equals(senderId) || chatRoom.getReceiver().getId().equals(senderId);
        if (!isChatRoomMember) {
            throw new InvalidChatMessageException("채팅방에 속해있지 않는 사용자입니다.");
        }
    }

    private void validateChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getMessage() == null || chatMessage.getMessage().isEmpty()) {
            throw new InvalidChatMessageException("메세지가 없습니다.");
        }
    }

    public SearchChatListResponseDto searchChatHistory(PrincipalDetails principalDetails, SearchRequestDto requestDto, Pageable pageable, String sortType) {
        Member loginMember = principalDetails.getMember();
        Slice<Chat> chatMessages = chatRepository.findByChatMessage(loginMember, requestDto.q(), pageable, sortType);
        List<SearchChatResponseDto> searchChatDtos = chatMessages.stream().map(
                chat -> {
                    Member chatMember = chat.getChatRoom().getSender().getId().equals(loginMember.getId()) ?
                            chat.getChatRoom().getReceiver() : chat.getChatRoom().getSender();
                    return SearchChatResponseDto.fromEntity(chat, chatMember);
                }
        ).collect(Collectors.toList());
        return SearchChatListResponseDto.from(chatMessages, searchChatDtos);
    }
}
