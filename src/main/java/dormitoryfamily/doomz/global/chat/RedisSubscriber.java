package dormitoryfamily.doomz.global.chat;

import static dormitoryfamily.doomz.global.chat.ChatProperties.CONSUMER_GROUP;

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
        String streamKey = message.getStream();
        String messageId = message.getId().getValue();

        try {
            // 메시지 데이터 추출
            Map<String, String> messageMap = message.getValue();

            // ChatMessage 생성
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageId(messageId);
            chatMessage.setRoomUUID(messageMap.get("roomUUID"));
            chatMessage.setSenderId(Long.parseLong(messageMap.get("senderId")));
            chatMessage.setMessage(messageMap.get("message"));

            // WebSocket 전송
            String destination = "/sub/chat/room/" + chatMessage.getRoomUUID();
            messagingTemplate.convertAndSend(destination, chatMessage);
            log.debug("[RedisSubscriber] Message sent to: {}", destination);

            // ACK 처리
            redisTemplate.opsForStream().acknowledge(streamKey, CONSUMER_GROUP, messageId);

        } catch (Exception e) {
            // → Pending 유지 (재시도 가능)
            log.error("[RedisSubscriber] Processing failed, will retry: {}", messageId, e);
        }
    }
}