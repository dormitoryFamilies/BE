package dormitoryfamily.doomz.global.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisSubscriber implements StreamListener<String, MapRecord<String, String, String>> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        try {
            Map<String, String> messageMap = message.getValue();

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setRoomUUID(messageMap.get("roomUUID"));

            String senderIdStr = messageMap.get("senderId");
            if (senderIdStr != null && !senderIdStr.isEmpty()) {
                chatMessage.setSenderId(Long.parseLong(senderIdStr));
            } else {
                log.error("[RedisSubscriber] senderId is null or empty in message: {}", messageMap);
                return;
            }

            chatMessage.setMessage(messageMap.get("message"));

            String destination = "/sub/chat/room/" + chatMessage.getRoomUUID();
            messagingTemplate.convertAndSend(destination, chatMessage);
            log.info("[RedisSubscriber] Message sent successfully to: {}", destination);

            // ACK 처리 (메시지 처리 완료)
            redisTemplate.opsForStream().acknowledge(
                    message.getStream(),
                    "chat-consumer-group",
                    message.getId()
            );

        } catch (Exception e) {
            log.error("Failed to process stream message: {}", e.getMessage(), e);
        }
    }
}
