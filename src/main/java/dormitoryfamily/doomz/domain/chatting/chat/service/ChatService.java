package dormitoryfamily.doomz.domain.chatting.chat.service;

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
import dormitoryfamily.doomz.global.chat.exception.InvalidChatMessageException;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.domain.notification.entity.type.NotificationType.CHAT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void saveChat(ChatMessage chatMessage) {
        ChatRoom chatRoom = getChatRoomByRoomUUID(chatMessage.getRoomUUID());

        Chat chat = ChatMessage.toEntity(chatMessage, chatRoom);

        chatRepository.save(chat);
        //알림 전송
        notifySavingChatInfo(chat);
    }

    private void notifySavingChatInfo(Chat chat) {
        eventPublisher.publishEvent(new ChatCreatedEvent(chat, CHAT));
    }

    private ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(ChatRoomNotExistsException::new);
    }


    public void deleteInvisibleChat(LocalDateTime enteredAt, String roomUUID) {
        chatRepository.deleteByCreatedAtBefore(roomUUID, enteredAt);

        // Redis Streams에서 입장 시간 이전 메시지 삭제
        String streamKey = "chat:stream:" + roomUUID;
        String maxStreamId = convertTimestampToStreamId(enteredAt);

        try {
            // XTRIM으로 해당 시간 이전 메시지 삭제
            List<org.springframework.data.redis.connection.stream.MapRecord<String, Object, Object>> oldMessages =
                redisTemplate.opsForStream().range(streamKey,
                    org.springframework.data.domain.Range.closed("-", maxStreamId));

            if (oldMessages != null && !oldMessages.isEmpty()) {
                org.springframework.data.redis.connection.stream.RecordId[] messageIds =
                    oldMessages.stream()
                        .map(org.springframework.data.redis.connection.stream.MapRecord::getId)
                        .toArray(org.springframework.data.redis.connection.stream.RecordId[]::new);

                redisTemplate.opsForStream().delete(streamKey, messageIds);
            }
        } catch (Exception e) {
            // Stream이 없거나 삭제 실패해도 무시 (MySQL은 이미 삭제됨)
        }
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
        LocalDateTime enteredAt = isInitiator ? chatRoom.getInitiatorEnteredAt() : chatRoom.getParticipantEnteredAt();

        Slice<Chat> chatSlice = chatRepository.findByChatRoomRoomUUIDAndCreatedAtAfter(roomUUID, enteredAt, pageable);

        List<ChatResponseDto> chatResponseDtos = chatSlice.getContent().stream()
            .map(chat -> {
                Member chatMember = Objects.equals(chat.getSenderId(), chatRoom.getInitiator().getId()) ?
                        chatRoom.getInitiator() : chatRoom.getParticipant();
                boolean isChatInitiator = chat.getSenderId().equals(loginMember.getId());
                return ChatResponseDto.fromEntity(chat, chatMember, isChatInitiator);
            })
            .collect(Collectors.toList());

        return ChatListResponseDto.from(pageable.getPageNumber(), !chatSlice.hasNext(), chatRoom.getRoomUUID(), chatResponseDtos);
    }

    private String convertTimestampToStreamId(LocalDateTime dateTime) {
        // LocalDateTime을 시스템 기본 시간대(KST)로 변환
        long timestamp = dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        return timestamp + "-0";
    }

    public void validateChat(ChatMessage chatMessage) {
        Long senderId = chatMessage.getSenderId();
        ChatRoom chatRoom = getChatRoomByUUID(chatMessage.getRoomUUID());

        validateMemberInChatRoom(chatRoom, senderId);
        validateChatMessageContent(chatMessage.getMessage());
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
                    ChatRoom chatRoom = findChatRoomByChat(chat);
                    return SearchChatResponseDto.fromEntity(chat, chatMember, chatRoom);
                })
                .collect(Collectors.toList());

        return SearchChatListResponseDto.from(chatMessages, searchChatDtos);
    }

    private Member determineChatMember(Chat chat, Member loginMember) {
        return Objects.equals(chat.getChatRoom().getInitiator().getId(), loginMember.getId()) ?
                chat.getChatRoom().getParticipant() : chat.getChatRoom().getInitiator();

    }

    private ChatRoom findChatRoomByChat(Chat chat) {
        return chatRoomRepository.findById(chat.getChatRoom().getId())
                .orElseThrow(ChatRoomNotExistsException::new);
    }
}
