package dormitoryfamily.doomz.global.chat;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import static dormitoryfamily.doomz.global.chat.ChatProperties.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamPoolInitializer {

    private final StreamMessageListenerContainer<String, ?> streamMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 스트림 풀 초기화 - 10개의 스트림에 대해 리스너 등록
     */
    @PostConstruct
    @SuppressWarnings("unchecked")
    public void initializeStreamPool() {
        for (int i = 0; i < STREAM_POOL_SIZE; i++) {
            String streamKey = STREAM_KEY_PREFIX + i;

            try {
                // Consumer Group 생성
                createConsumerGroupIfNotExists(streamKey);

                // Stream 리스너 등록
                ((StreamMessageListenerContainer<String, MapRecord<String, String, String>>) streamMessageListenerContainer)
                        .receive(
                                Consumer.from(CONSUMER_GROUP, CONSUMER_NAME),
                                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                                redisSubscriber
                        );

                log.info("[StreamPoolInitializer] Initialized stream pool: {}", streamKey);
            } catch (Exception e) {
                log.error("[StreamPoolInitializer] Failed to initialize stream pool: {}", streamKey, e);
            }
        }
    }

    private void createConsumerGroupIfNotExists(String streamKey) {
        try {
            // MKSTREAM 옵션으로 Stream + Consumer Group 동시 생성
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.streamCommands().xGroupCreate(
                        streamKey.getBytes(),
                        CONSUMER_GROUP,
                        ReadOffset.latest(),
                        true  // MKSTREAM: Stream이 없으면 자동 생성
                );
                return null;
            });
        } catch (Exception e) {
            log.debug("[StreamPoolInitializer] Consumer group already exists for stream: {}", streamKey);
        }
    }
}
