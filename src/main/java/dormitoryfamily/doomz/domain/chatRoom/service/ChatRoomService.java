package dormitoryfamily.doomz.domain.chatRoom.service;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.exception.ChatNotExistsException;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.service.ChatService;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.*;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.exception.*;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.chat.ChatMessage;
import dormitoryfamily.doomz.global.chat.RedisSubscriber;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ChatService chatService;

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        topics = new HashMap<>();
    }

    public CreateChatRoomResponseDto createChatRoom(Long memberId, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Member chatMember = getMemberById(memberId);
        checkSenderReceiverDistinct(loginMember, chatMember);

        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderAndReceiver(loginMember, chatMember);

        if (chatRoom.isPresent()) {
            ChatRoom room = chatRoom.get();
            updateChatRoomEntryStatus(room, loginMember);
            return CreateChatRoomResponseDto.fromEntity(room);
        } else {
            ChatRoom room = ChatRoom.create(loginMember, chatMember);
            chatRoomRepository.save(room);
            return CreateChatRoomResponseDto.fromEntity(room);
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void checkSenderReceiverDistinct(Member loginMember, Member chatMember) {
        if (loginMember.getId().equals(chatMember.getId())) {
            throw new CannotChatYourselfException();
        }
    }

    private void updateChatRoomEntryStatus(ChatRoom chatRoom, Member loginMember) {
        boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());

        if (isSender && chatRoom.getSenderEnteredAt() == null) {
            chatRoom.reEnterSender();
        } else if (!isSender && chatRoom.getReceiverEnteredAt() == null) {
            chatRoom.reEnterReceiver();
        } else {
            throw new AlreadyInChatRoomException();
        }
    }

    public void joinChatRoom(String roomUUID) {
        ChannelTopic topic = topics.get(roomUUID);

        if (topic == null) {
            topic = new ChannelTopic(roomUUID);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomUUID, topic);
        }
    }

    public ChannelTopic getTopic(String roomUUID) {
        return topics.get(roomUUID);
    }

    public void updateUnreadCount(ChatMessage chatMessage) {
        ChatRoom chatRoom = getChatRoomByRoomUUID(chatMessage.getRoomUUID());

        if (chatMessage.getSenderId().equals(chatRoom.getSender().getId())) {
            updateReceiverUnreadCount(chatRoom);
        } else {
            updateSenderUnreadCount(chatRoom);
        }
    }

    private void updateReceiverUnreadCount(ChatRoom chatRoom) {
        if (chatRoom.getReceiverStatus() == OUT) {
            chatRoom.increaseReceiverUnreadCount();
        }
    }

    private void updateSenderUnreadCount(ChatRoom chatRoom) {
        if (chatRoom.getSenderStatus() == OUT) {
            chatRoom.increaseSenderUnreadCount();
        }
    }

    public void deleteChatRoom(Long roomId, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        validateMemberAccess(chatRoom, loginMember);

        boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());
        boolean isSenderDeleted = chatRoom.getSenderEnteredAt() == null;
        boolean isReceiverDeleted = chatRoom.getReceiverEnteredAt() == null;

        validateRoomLeft(isSender, isSenderDeleted, isReceiverDeleted);
        deleteOrChangeChatRoomStatus(chatRoom, isSender, isSenderDeleted, isReceiverDeleted);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    private void validateMemberAccess(ChatRoom chatRoom, Member loginMember) {
        if (!chatRoom.getSender().getId().equals(loginMember.getId()) &&
                !chatRoom.getReceiver().getId().equals(loginMember.getId())) {
            throw new InvalidMemberAccessException();
        }
    }

    private void validateRoomLeft(boolean isSender, boolean isSenderDeleted, boolean isReceiverDeleted) {
        if ((isSender && isSenderDeleted) || (!isSender && isReceiverDeleted)) {
            throw new AlreadyChatRoomLeftException();
        }
    }

    private void deleteOrChangeChatRoomStatus(ChatRoom chatRoom, boolean isSender, boolean isSenderDeleted, boolean isReceiverDeleted) {
        if (isSenderDeleted || isReceiverDeleted) {
            chatRoomRepository.delete(chatRoom);
        } else {
            if (isSender) {
                chatRoom.deleteSender();
            } else {
                chatRoom.deleteReceiver();
            }
            chatService.clearChatIfNeed(isSender ? chatRoom.getReceiverEnteredAt() : chatRoom.getSenderEnteredAt(), chatRoom.getRoomUUID());
        }
    }

    public void deleteEmptyChatRoom(Long roomId) {
        ChatRoom chatRoom = getChatRoomById(roomId);
        checkIfChatRoomIsEmpty(chatRoom.getRoomUUID());
        chatRoomRepository.delete(chatRoom);
    }

    private void checkIfChatRoomIsEmpty(String roomUUID){
        if(chatRepository.existsByChatRoomRoomUUID(roomUUID)){
            throw new ChatRoomNotEmptyException();
        }
    }

    public ChatRoomListResponseDto findAllChatRooms(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        Slice<ChatRoom> chatRooms = chatRoomRepository.findAllByMember(loginMember, pageable);
        List<ChatRoomResponseDto> chatRoomDtos = createChatRoomResponseDtos(chatRooms.stream().toList(), loginMember);
        chatRoomDtos.sort(Comparator.comparing(ChatRoomResponseDto::lastMessageTime).reversed());
        return ChatRoomListResponseDto.toDto(chatRooms, chatRoomDtos);
    }

    private List<ChatRoomResponseDto> createChatRoomResponseDtos(List<ChatRoom> chatRooms, Member loginMember) {
        return chatRooms.stream()
                .map(chatRoom -> {
                    Chat lastChat = getLastChatByRoomUUID(chatRoom.getRoomUUID());
                    boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());
                    return ChatRoomResponseDto.fromEntity(chatRoom, lastChat, isSender);
                })
                .collect(Collectors.toList());
    }

    private Chat getLastChatByRoomUUID(String roomUUID) {
        return chatRepository.findTopByChatRoomRoomUUIDOrderByCreatedAtDesc(roomUUID).orElseThrow(ChatNotExistsException::new);
    }

    public void exitChatRoom(PrincipalDetails principalDetails, Long roomId) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        setChatMemberStatusOut(chatRoom, loginMember);
    }

    private void setChatMemberStatusOut(ChatRoom chatRoom, Member loginMember) {
        if (chatRoom.getSender().getId().equals(loginMember.getId())) {
            chatRoom.setSenderStatusOut();
        } else {
            chatRoom.setReceiverStatusOut();
        }
    }

    private ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    public UnreadChatCountResponseDto countTotalUnreadChat(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        int totalCount = chatRoomRepository.findTotalUnreadCountForMember(loginMember);
        return UnreadChatCountResponseDto.toDto(totalCount);
    }


    public ChatRoomListResponseDto searchChatRooms(PrincipalDetails principalDetails, SearchRequestDto requestDto, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        Slice<ChatRoom> chatRooms = chatRoomRepository.findByMemberAndNickname(loginMember, requestDto.q(), pageable);
        List<ChatRoomResponseDto> chatRoomDtos = createChatRoomResponseDtos(chatRooms.stream().toList(), loginMember);
        chatRoomDtos.sort(Comparator.comparing(ChatRoomResponseDto::lastMessageTime).reversed());
        return ChatRoomListResponseDto.toDto(chatRooms, chatRoomDtos);
    }

    public ChatRoomIdResponseDto findChatRoomByMember(Long memberId, PrincipalDetails principalDetails) {
        Member loggedInMember = principalDetails.getMember();
        Member chatMember = getMemberById(memberId);

        ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(loggedInMember, chatMember)
                .orElseThrow(MemberChatRoomNotExistsException::new);

        return ChatRoomIdResponseDto.fromEntity(chatRoom);
    }
}