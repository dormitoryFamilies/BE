package dormitoryfamily.doomz.domain.chatting.chat.service;

import dormitoryfamily.doomz.domain.chatting.chat.dto.ChatDto;
import dormitoryfamily.doomz.domain.chatting.chat.dto.response.ChatListResponseDto;
import dormitoryfamily.doomz.domain.chatting.chat.dto.response.ChatResponseDto;
import dormitoryfamily.doomz.domain.chatting.chat.dto.response.SearchChatListResponseDto;
import dormitoryfamily.doomz.domain.chatting.chat.dto.response.SearchChatResponseDto;
import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatting.chat.event.ChatCreatedEvent;
import dormitoryfamily.doomz.domain.chatting.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chatting.chatroom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatting.chatroom.exception.AlreadyChatRoomLeftException;
import dormitoryfamily.doomz.domain.chatting.chatroom.exception.ChatRoomNotExistsException;
import dormitoryfamily.doomz.domain.chatting.chatroom.exception.MemberNotInChatRoomException;
import dormitoryfamily.doomz.domain.chatting.chatroom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.global.chat.ChatMessage;
import dormitoryfamily.doomz.global.chat.ChatProperties;
import dormitoryfamily.doomz.global.chat.exception.InvalidChatMessageException;
import dormitoryfamily.doomz.global.chat.moderation.ModerationResult;
import dormitoryfamily.doomz.global.chat.moderation.ModerationService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

import static dormitoryfamily.doomz.domain.notification.entity.type.NotificationType.CHAT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, ChatDto> redisTemplateMessage;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ModerationService moderationService;

    public void saveChat(ChatMessage chatMessage) {
        try {
            ChatRoom chatRoom = getChatRoomByRoomUUID(chatMessage.getRoomUUID());

            Chat chat = ChatMessage.toEntity(chatMessage, chatRoom);

            chatRepository.save(chat);
            saveChatInRedis(chatMessage.getRoomUUID(), chat);
            //알림 전송
            notifySavingChatInfo(chat);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.warn("[ChatService] Duplicate message detected and ignored: messageId={}", chatMessage.getMessageId());
        }
    }

    private void notifySavingChatInfo(Chat chat) {
        eventPublisher.publishEvent(new ChatCreatedEvent(chat, CHAT));
    }

    private ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    private void saveChatInRedis(String roomUUID, Chat chat) {
        String cacheKey = ChatProperties.getCacheKey(roomUUID);
        ChatDto chatDto = ChatDto.fromEntity(chat);
        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));

        ZSetOperations<String, ChatDto> zSetOps = redisTemplateMessage.opsForZSet();
        zSetOps.add(cacheKey, chatDto, chat.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
        redisTemplateMessage.expire(cacheKey, 1, TimeUnit.DAYS);
    }


    public void deleteInvisibleChat(LocalDateTime enteredAt, String roomUUID) {
        chatRepository.deleteByCreatedAtBefore(roomUUID, enteredAt);
        String cacheKey = ChatProperties.getCacheKey(roomUUID);
        redisTemplateMessage.delete(cacheKey);
    }

    public ChatListResponseDto findAllChatHistory(PrincipalDetails principalDetails, Long roomId, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);

        boolean isInitiator = Objects.equals(chatRoom.getInitiator().getId(), loginMember.getId());

        validateChatRoomAccessAndStatus(chatRoom, loginMember, isInitiator);
        updateMemberStatusToIn(chatRoom, isInitiator);

        return createChatListResponse(chatRoom, loginMember, isInitiator, pageable);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    private void validateChatRoomAccessAndStatus(ChatRoom chatRoom, Member loginMember, boolean isInitiator) {
        if (!isInitiator && !Objects.equals(chatRoom.getParticipant().getId(), loginMember.getId())) {
            throw new MemberNotInChatRoomException();
        }

        if ((isInitiator && chatRoom.getInitiatorEnteredAt() == null) || (!isInitiator && chatRoom.getParticipantEnteredAt() == null)) {
            throw new AlreadyChatRoomLeftException();
        }
    }

    private void updateMemberStatusToIn(ChatRoom chatRoom, boolean isInitiator) {
        if (isInitiator) {
            chatRoom.initiatorInChatRoom();
        } else {
            chatRoom.participantInChatRoom();
        }
    }

    private ChatListResponseDto createChatListResponse(ChatRoom chatRoom, Member loginMember, boolean isInitiator, Pageable pageable) {
        String roomUUID = chatRoom.getRoomUUID();
        String cacheKey = ChatProperties.getCacheKey(roomUUID);
        ZSetOperations<String, ChatDto> zSetOps = redisTemplateMessage.opsForZSet();

        Long cachedMessageCount = zSetOps.zCard(cacheKey);

        //redis에 캐시된 값이 없으면 db에서 redis로 보냄
        if (cachedMessageCount == 0) {
            cacheChatMessagesFromDB(roomUUID, cacheKey, zSetOps);
        }

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        double startScore = calculateStartScore(chatRoom, isInitiator);
        double endScore = Double.POSITIVE_INFINITY;

        //입장 시간 이후에 해당 되는 채팅 개수 카운트
        Long sizeInRange = zSetOps.count(cacheKey, startScore, endScore);

        //아직 반환하지 않은 채팅 개수 카운트
        long remainingCount = sizeInRange - ((long) pageSize * pageNumber);
        //이미 다 반환한 경우에는 공백으로 반환
        if (remainingCount < 0) {
            return ChatListResponseDto.from(pageNumber, true, roomUUID, Collections.emptyList());
        }

        long offset = Math.max(sizeInRange - (long) (pageNumber + 1) * pageSize, 0);
        long count = Math.min(remainingCount, pageable.getPageSize());

        //해당 score에 포함되는 chat 중 offset부터 count 만큼 가져옴
        Set<ChatDto> chatSet = zSetOps.rangeByScore(cacheKey, startScore, endScore, offset, count);

        List<ChatResponseDto> chatResponseDtos = CreateChatResponseDtoList(chatSet, chatRoom, loginMember);
        boolean isLast = chatResponseDtos.size() < pageSize;
        return ChatListResponseDto.from(pageNumber, isLast, roomUUID, chatResponseDtos);
    }

    private void cacheChatMessagesFromDB(String roomUUID, String cacheKey, ZSetOperations<String, ChatDto> zSetOps) {
        List<Chat> dbChatList = chatRepository.findAllByChatRoomRoomUUID(roomUUID);
        dbChatList.stream()
                .map(ChatDto::fromEntity)
                .forEach(chatDto -> {
                    zSetOps.add(cacheKey, chatDto, chatDto.sentTime().toEpochSecond(ZoneOffset.UTC));
                    redisTemplateMessage.expire(cacheKey, 1, TimeUnit.DAYS);
                });
    }

    private double calculateStartScore(ChatRoom chatRoom, boolean isInitiator) {
        return isInitiator ? chatRoom.getInitiatorEnteredAt().toEpochSecond(ZoneOffset.UTC) :
                chatRoom.getParticipantEnteredAt().toEpochSecond(ZoneOffset.UTC);
    }

    private List<ChatResponseDto> CreateChatResponseDtoList(Set<ChatDto> chatSet, ChatRoom chatRoom, Member loginMember) {
        List<ChatDto> chatList = new ArrayList<>(chatSet);
        return chatList.stream()
                .map(chat -> {
                    Member chatMember = Objects.equals(chat.senderId(), chatRoom.getInitiator().getId()) ?
                            chatRoom.getInitiator() : chatRoom.getParticipant();
                    boolean isChatInitiator = chat.senderId().equals(loginMember.getId());
                    return ChatResponseDto.fromChatDto(chat, chatMember, isChatInitiator);
                })
                .collect(Collectors.toList());
    }

    public void validateChat(ChatMessage chatMessage) {
        Long senderId = chatMessage.getSenderId();
        ChatRoom chatRoom = getChatRoomByUUID(chatMessage.getRoomUUID());

        validateMemberInChatRoom(chatRoom, senderId);
        validateChatMessageContent(chatMessage.getMessage());
        validateMessageModeration(chatMessage.getMessage());
    }

    private void validateMessageModeration(String message) {
        ModerationResult result = moderationService.moderateMessage(message);

        if (result.isFlagged()) {
            String categories = String.join(", ", result.getCategories());
            throw new InvalidChatMessageException("부적절한 내용이 포함된 메시지입니다: " + categories);
        }
    }

    //웹 소켓 요청에 대한 예외
    private ChatRoom getChatRoomByUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(() -> new InvalidChatMessageException("존재하지 않는 채팅방입니다."));
    }

    private void validateMemberInChatRoom(ChatRoom chatRoom, Long senderId) {
        if (!isMemberInChatRoom(chatRoom, senderId)) {
            throw new InvalidChatMessageException("채팅방에 속해있지 않는 사용자입니다.");
        }

        if (isInitiator(chatRoom, senderId) && chatRoom.getInitiatorEnteredAt() == null) {
            throw new InvalidChatMessageException("발송자가 해당 채팅방에 나가있는 상태입니다.");
        }
    }

    private boolean isMemberInChatRoom(ChatRoom chatRoom, Long memberId) {
        return isInitiator(chatRoom, memberId) || isParticipant(chatRoom, memberId);
    }

    private boolean isInitiator(ChatRoom chatRoom, Long memberId) {
        return Objects.equals(chatRoom.getInitiator().getId(), memberId);
    }

    private boolean isParticipant(ChatRoom chatRoom, Long memberId) {
        return Objects.equals(chatRoom.getParticipant().getId(), memberId);
    }

    private void validateChatMessageContent(String message) {
        if (message == null || message.isEmpty()) {
            throw new InvalidChatMessageException("메세지가 없습니다.");
        }
    }

    public SearchChatListResponseDto searchChatHistory(PrincipalDetails principalDetails, SearchRequestDto requestDto, Pageable pageable, String sortType) {
        Member loginMember = principalDetails.getMember();

        Slice<Chat> chatMessages = chatRepository.findByChatMessage(loginMember, requestDto.q(), pageable, sortType);

        List<SearchChatResponseDto> searchChatDtos = chatMessages.stream()
                .map(chat -> {
                    Member chatMember = determineChatMember(chat, loginMember);
                    ChatRoom chatRoom = chat.getChatRoom();
                    return SearchChatResponseDto.fromEntity(chat, chatMember, chatRoom);
                })
                .collect(Collectors.toList());

        return SearchChatListResponseDto.from(chatMessages, searchChatDtos);
    }

    private Member determineChatMember(Chat chat, Member loginMember) {
        return Objects.equals(chat.getChatRoom().getInitiator().getId(), loginMember.getId()) ?
                chat.getChatRoom().getParticipant() : chat.getChatRoom().getInitiator();
    }
}
