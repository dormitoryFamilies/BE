package dormitoryfamily.doomz.domain.chatRoom.service;

import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.exception.ChatNotExistsException;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.service.ChatService;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.ChatRoomListResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.ChatRoomResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.CreateChatRoomResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatRoomStatus;
import dormitoryfamily.doomz.domain.chatRoom.exception.AlreadyChatRoomLeftException;
import dormitoryfamily.doomz.domain.chatRoom.exception.CannotChatYourselfException;
import dormitoryfamily.doomz.domain.chatRoom.exception.ChatRoomNotExistsException;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.chatRoom.dto.ChatRoomDto;
import dormitoryfamily.doomz.global.chat.ChatMessage;
import dormitoryfamily.doomz.global.chat.RedisSubscriber;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

    private static final String Chat_Rooms = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoomDto> opsHashChatRoom;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public CreateChatRoomResponseDto createChatRoom(Long memberId, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Member chatMember = getMemberById(memberId);
        checkSenderReceiverDistinct(loginMember, chatMember);

        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderAndReceiver(loginMember, chatMember);

        if (chatRoom.isPresent()) {
            ChatRoom room = chatRoom.get();
            updateChatRoomStatusIfNeeded(room, loginMember);
            return CreateChatRoomResponseDto.fromEntity(room);
        } else {
            ChatRoom createdChatRoom = ChatRoom.create(loginMember, chatMember);
            ChatRoom savedChatRoom = chatRoomRepository.save(createdChatRoom);
            saveChatRoomToRedis(savedChatRoom);
            return CreateChatRoomResponseDto.fromEntity(savedChatRoom);
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

    private void saveChatRoomToRedis(ChatRoom chatRoom) {
        ChatRoomDto chatRoomDto = ChatRoomDto.fromEntity(chatRoom);
        opsHashChatRoom.put(Chat_Rooms, chatRoomDto.getRoomUUID(), chatRoomDto);
    }

    private void updateChatRoomStatusIfNeeded(ChatRoom room, Member loginMember) {
        boolean isSender = room.getSender().getId().equals(loginMember.getId());
        boolean isReceiver = room.getReceiver().getId().equals(loginMember.getId());
        if ((isSender && room.getChatRoomStatus().equals(ChatRoomStatus.ONLY_RECEIVER)) ||
                (isReceiver && room.getChatRoomStatus().equals(ChatRoomStatus.ONLY_SENDER))) {
            room.changeChatRoomStatus(ChatRoomStatus.BOTH);
            saveChatRoomToRedis(room);
        }
    }

    public void deleteChatRoom(Long roomId, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        deleteChatRoomProcess(chatRoom, loginMember);
        setChatMemberStatusOut(chatRoom, loginMember);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    private void deleteChatRoomProcess(ChatRoom chatRoom, Member loginMember) {

        boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());
        boolean isSenderDeleted = chatRoom.getChatRoomStatus().equals(ChatRoomStatus.ONLY_RECEIVER);
        boolean isReceiverDeleted = chatRoom.getChatRoomStatus().equals(ChatRoomStatus.ONLY_SENDER);

        if (isSender) {
            if (isSenderDeleted) {
                throw new AlreadyChatRoomLeftException();
            } else if (isReceiverDeleted) {
                deleteChatRoom(chatRoom);
            } else {
                changeChatStatusAndClearChatIfNeed(chatRoom, true);
            }
        } else {
            if (isReceiverDeleted) {
                throw new AlreadyChatRoomLeftException();
            } else if (isSenderDeleted) {
                deleteChatRoom(chatRoom);
            } else {
                changeChatStatusAndClearChatIfNeed(chatRoom, false);
            }
        }
    }

    private void setChatMemberStatusOut(ChatRoom chatRoom, Member loginMember) {
        if (chatRoom.getSender().getId().equals(loginMember.getId())) {
            chatRoom.setSenderStatusOut();
        } else {
            chatRoom.setReceiverStatusOut();
        }
    }

    private  void changeChatStatusAndClearChatIfNeed(ChatRoom chatRoom, boolean isSender){
        Chat lastChat = getLastChatByRoomUUID(chatRoom.getRoomUUID());
        if(isSender){
            if(chatRoom.getLastSenderOnlyChatId() != null){
                chatService.clearChat(chatRoom.getLastSenderOnlyChatId(), chatRoom.getRoomUUID());
            }
            chatRoom.deleteSender(lastChat.getId());
        }
        else{
            if(chatRoom.getLastReceiverOnlyChatId()!=null){
                chatService.clearChat(chatRoom.getLastReceiverOnlyChatId(), chatRoom.getRoomUUID());
            }
            chatRoom.deleteReceiver(lastChat.getId());
        }
        saveChatRoomToRedis(chatRoom);
    }

    private Chat getLastChatByRoomUUID(String roomUUID) {
        return chatRepository.findTopByChatRoomRoomUUIDOrderByCreatedAtDesc(roomUUID).orElseThrow(ChatNotExistsException::new);
    }

    private void deleteChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
        opsHashChatRoom.delete(Chat_Rooms, chatRoom.getRoomUUID());
    }

    public void enterChatRoom(String roomUUID) {
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

    public ChatRoomListResponseDto findAllChatRooms(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMember(loginMember);
        List<ChatRoomResponseDto> responseDtos = createChatRoomResponseDtos(chatRooms, loginMember);
        responseDtos.sort(Comparator.comparing(ChatRoomResponseDto::lastMessageTime).reversed());
        return ChatRoomListResponseDto.toDto(responseDtos);
    }

    private List<ChatRoomResponseDto> createChatRoomResponseDtos(List<ChatRoom> chatRooms, Member loginMember) {
        return chatRooms.stream()
                .map(chatRoom -> {
                    Chat lastChat = getLastChatByRoomUUID(chatRoom.getRoomUUID());
                    if (chatRoom.getSender().getId().equals(loginMember.getId())) {
                        return ChatRoomResponseDto.fromEntityWhenSender(chatRoom, lastChat);
                    } else {
                        return ChatRoomResponseDto.fromEntityWhenReceiver(chatRoom, lastChat);
                    }
                })
                .collect(Collectors.toList());
    }

    public void exitChatRoom(PrincipalDetails principalDetails, Long roomId) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        setChatMemberStatusOut(chatRoom, loginMember);
    }

    public void updateUnreadCount(ChatMessage chatMessage) {
        ChatRoom chatRoom = getChatRoomByRoomUUID(chatMessage.getRoomUUID());

        if (chatMessage.getSenderId().equals(chatRoom.getSender().getId())) {
            updateReceiverUnreadCount(chatRoom);
        } else {
            updateSenderUnreadCount(chatRoom);
        }
    }

    private ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(ChatRoomNotExistsException::new);
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
}


