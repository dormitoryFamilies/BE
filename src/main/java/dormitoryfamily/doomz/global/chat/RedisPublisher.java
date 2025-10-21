package dormitoryfamily.doomz.global.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String streamKey, ChatMessage chatMessage) {
        try {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("roomUUID", chatMessage.getRoomUUID());
            messageMap.put("senderId", String.valueOf(chatMessage.getSenderId()));
            messageMap.put("message", chatMessage.getMessage());
            redisTemplate.opsForStream().add(streamKey, messageMap);
            redisTemplate.opsForStream().trim(streamKey, 10000, true);
        } catch (Exception e) {
            log.error("Failed to publish message to Redis Stream: {}", e.getMessage(), e);
        }
    }
}