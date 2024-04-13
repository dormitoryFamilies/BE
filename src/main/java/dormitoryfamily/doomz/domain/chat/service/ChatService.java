package dormitoryfamily.doomz.domain.chat.service;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, ChatDto> redisTemplateMessage;
    private final ChatRepository chatRepository;

    public void saveChat(ChatDto chatDto) {
        Chat chat = ChatDto.toEntity(chatDto);
        chatRepository.save(chat);

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Chat.class));
        redisTemplateMessage.opsForList().rightPush(chatDto.getRoomUUID(), chatDto);
        redisTemplateMessage.expire(chatDto.getRoomUUID(), 1, TimeUnit.MINUTES);
    }

    public void clearChat(Long lastChatId, String roomUUID) {
       chatRepository.deleteChatsLessThanChatId(lastChatId);
       redisTemplateMessage.delete(roomUUID);  //다시 db에서 불러오기 위해 전체 삭제
    }
}
