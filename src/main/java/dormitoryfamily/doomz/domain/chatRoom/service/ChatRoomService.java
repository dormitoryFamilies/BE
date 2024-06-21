package dormitoryfamily.doomz.domain.chatRoom.service;

import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.CreateChatRoomResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.exception.CannotChatYourselfException;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.redis.ChatRoomEntity;
import dormitoryfamily.doomz.global.redis.RedisSubscriber;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

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

            try {
                // 예외가 발생할 수 있는 코드 블록
                // 예를 들어, 데이터베이스 연결이나 외부 API 호출
                // 여기서는 Redis에 데이터를 저장하는 부분일 수 있습니다.
                opsHashChatRoom.put(Chat_Rooms, chatRoomEntity.roomUUID(), chatRoomEntity);
            } catch (Exception e) {
                // 예외가 발생했을 때 실행될 코드 블록
                // 예외를 처리하고 오류 메시지를 기록하거나 다른 조치를 취할 수 있습니다.
                e.printStackTrace(); // 또는 로깅 프레임워크를 사용하여 로그를 남길 수 있습니다.
            }
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
}
