package dormitoryfamily.doomz.domain.chatRoom.service;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.service.ChatService;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.ChatRoomListResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.CreateChatRoomResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberType;
import dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatRoomStatus;
import dormitoryfamily.doomz.domain.chatRoom.exception.AlreadyChatRoomLeftException;
import dormitoryfamily.doomz.domain.chatRoom.exception.CannotChatYourselfException;
import dormitoryfamily.doomz.domain.chatRoom.exception.ChatRoomNotExistsException;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.redis.ChatRoomEntity;
import dormitoryfamily.doomz.global.redis.RedisSubscriber;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatService chatService;

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

    private static final String Chat_Rooms = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoomEntity> opsHashChatRoom;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public CreateChatRoomResponseDto createChatRoom(Long memberId, PrincipalDetails principalDetails) {
        Member loginMember  = principalDetails.getMember();
        Member chatMember = getMemberById(memberId);
        checkSenderReceiverDistinct(loginMember, chatMember);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderAndReceiver(loginMember, chatMember);

        if(chatRoom.isPresent()){
            return CreateChatRoomResponseDto.fromEntity(chatRoom.get());
        }
        else {

            ChatRoom createdchatRoom = ChatRoom.create(loginMember, chatMember);
            ChatRoom savedChatRoom = chatRoomRepository.save(createdchatRoom);

            ChatRoomEntity chatRoomEntity = ChatRoomEntity.create(savedChatRoom);
            opsHashChatRoom.put(Chat_Rooms, chatRoomEntity.roomUUID(), chatRoomEntity);

            return CreateChatRoomResponseDto.fromEntity(savedChatRoom);
        }

    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void checkSenderReceiverDistinct(Member loginMember, Member chatMember){
        if (loginMember.getId().equals(chatMember.getId())) {
            throw new CannotChatYourselfException();
        }
    }

    public void deleteChatRoom(Long memberId, PrincipalDetails principalDetails) {
        Member loginMember  = principalDetails.getMember();
        Member chatMember = getMemberById(memberId);
        ChatRoom chatRoom = getChatRoomBySenderAndReceiver(loginMember, chatMember);
        deleteChatRoomProcess(chatRoom, loginMember);
    }

    private ChatRoom getChatRoomBySenderAndReceiver(Member loginMember, Member chatMember) {
        return chatRoomRepository.findBySenderAndReceiver(loginMember, chatMember)
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
                changeChatStatusAndDeleteSender(chatRoom);
            }
        } else {
            if (isReceiverDeleted) {
                throw new AlreadyChatRoomLeftException();
            } else if (isSenderDeleted) {
                deleteChatRoom(chatRoom);
            } else {
                changeChatStatusAndDeleteReceiver(chatRoom);
            }
        }
    }

    private void changeChatStatusAndDeleteSender(ChatRoom chatRoom) {
        chatService.changeChatStatus(chatRoom, SENDER);
        chatRoom.deleteSender();
    }

    private void changeChatStatusAndDeleteReceiver(ChatRoom chatRoom) {
        chatService.changeChatStatus(chatRoom, RECEIVER);
        chatRoom.deleteReceiver();
    }

    private void deleteChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
        opsHashChatRoom.delete(Chat_Rooms, chatRoom.getRoomUUID());
    }
}
