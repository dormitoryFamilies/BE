package dormitoryfamily.doomz.domain.chat.service;

import dormitoryfamily.doomz.domain.chat.dto.response.ChatListResponseDto;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.exception.ChatRoomNotExistsException;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, ChatDto> redisTemplateMessage;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveChat(ChatDto chatDto) {
        Chat chat = ChatDto.toEntity(chatDto);
        Chat savedChat = chatRepository.save(chat);
        chatDto.setChatIdAndSentTime(savedChat.getId(), savedChat.getCreatedAt());

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));
        redisTemplateMessage.opsForList().rightPush(chatDto.getRoomUUID(), chatDto);
        redisTemplateMessage.expire(chatDto.getRoomUUID(), 1, TimeUnit.MINUTES);
    }

    public void clearChat(Long lastChatId, String roomUUID) {
       chatRepository.deleteChatsLessThanChatId(lastChatId);
       redisTemplateMessage.delete(roomUUID);  //다시 db에서 불러오기 위해 전체 삭제
    }

    public ChatListResponseDto findAllChatHistory(PrincipalDetails principalDetails, Long roomId) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        String roomUUID = chatRoom.getRoomUUID();
        List<ChatDto> chatList = new LinkedList<>();
        try {
            chatList = redisTemplateMessage.opsForList().range(roomUUID, 0, 99);

            // 4. Redis 에서 가져온 메시지가 없다면, DB 에서 메시지 100개 가져오기
            if (chatList == null || chatList.isEmpty()) {
                List<Chat> dbChatList = chatRepository.findAllByRoomUUID(roomUUID);

                for (Chat chat : dbChatList) {
                    ChatDto chatDto = ChatDto.fromEntity(chat);
                    chatList.add(chatDto);
                    redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));      // 직렬화
                    redisTemplateMessage.opsForList().rightPush(roomUUID, chatDto);                                // redis 저장
                }
            }

            boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());
            if (isSender && chatRoom.getLastReceiverOnlyChatId() != null) {
                chatList.removeIf(chatDto -> chatDto.getChatId() <= chatRoom.getLastReceiverOnlyChatId());
            } else if (!isSender && chatRoom.getLastSenderOnlyChatId() != null) {
                chatList.removeIf(chatDto -> chatDto.getChatId() <= chatRoom.getLastReceiverOnlyChatId());
            }
            return ChatListResponseDto.toDto(chatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ChatListResponseDto.toDto(chatList);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotExistsException::new);
    }
}
