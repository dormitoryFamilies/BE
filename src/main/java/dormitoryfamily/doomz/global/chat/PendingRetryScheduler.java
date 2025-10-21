package dormitoryfamily.doomz.global.chat;

import static dormitoryfamily.doomz.global.chat.ChatProperties.CONSUMER_GROUP;
import static dormitoryfamily.doomz.global.chat.ChatProperties.CONSUMER_NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class PendingRetryScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisSubscriber redisSubscriber;

    private static final int MAX_RETRY_COUNT = 1;
    private static final long PENDING_THRESHOLD_MILLIS = 10000;

    @Scheduled(initialDelay = 0, fixedDelay = 30000)
    public void retryPendingMessages() {
        // 모든 채팅 스트림 키 조회
        Set<String> streamKeys = findAllChatStreams();
        // 스트림 키가 없으면 종료
        if (streamKeys.isEmpty()) {
            return;
        }

        // 각 스트림의 Pending 메시지 처리
        for (String streamKey : streamKeys) {
            processPendingMessages(streamKey);
        }
    }

    private void processPendingMessages(String streamKey) {
        try {
            List<MapRecord<String, String, String>> messages = claimRetryableMessages(streamKey);
            messages.forEach(redisSubscriber::onMessage);
        } catch (Exception e) {
            log.error("[PendingRetryScheduler] Error processing stream: {}", streamKey, e);
        }
    }

    private List<MapRecord<String, String, String>> claimRetryableMessages(String streamKey) {
        try {
            // Pending 메시지 조회
            PendingMessages pendingMessages = redisTemplate.opsForStream()
                    .pending(streamKey, CONSUMER_GROUP, Range.unbounded(), 100L);

            if (pendingMessages == null || pendingMessages.isEmpty()) {
                return Collections.emptyList();
            }

            // 재시도 가능한 메시지 ID 수집
            List<RecordId> retryableIds = new ArrayList<>();
            for (PendingMessage pending : pendingMessages) {
                long deliveryCount = pending.getTotalDeliveryCount();
                long idleTime = pending.getElapsedTimeSinceLastDelivery().toMillis();

                // 최대 재시도 횟수 초과 시 DLQ로 이동
                if (deliveryCount >= MAX_RETRY_COUNT) {
                    moveToDLQ(streamKey, pending);
                } else if (idleTime >= PENDING_THRESHOLD_MILLIS) { // 재시도 대상
                    retryableIds.add(RecordId.of(pending.getIdAsString()));
                }
            }

            // 재시도 가능한 메시지가 없으면 종료
            if (retryableIds.isEmpty()) {
                return Collections.emptyList();
            }

            // 메시지 클레임
            List<MapRecord<String, Object, Object>> claimed = redisTemplate.opsForStream()
                    .claim(streamKey, CONSUMER_GROUP, CONSUMER_NAME,
                            Duration.ofMillis(PENDING_THRESHOLD_MILLIS),
                            retryableIds.toArray(new RecordId[0]));

            // 문자열 형태로 변환하여 반환
            return claimed.stream()
                    .map(this::convertToStringRecord)
                    .toList();

        } catch (Exception e) {
            log.error("[PendingRetryScheduler] Failed to claim messages: {}", streamKey, e);
            return Collections.emptyList();
        }
    }

    private void moveToDLQ(String streamKey, PendingMessage pending) {
        String messageId = pending.getIdAsString();

        try {
            // 메시지 클레임
            List<MapRecord<String, Object, Object>> claimed = redisTemplate.opsForStream()
                    .claim(streamKey, CONSUMER_GROUP, CONSUMER_NAME, Duration.ZERO, RecordId.of(messageId));

            // DLQ 데이터 준비
            Map<String, Object> dlqData = copyMapData(claimed.get(0).getValue());
            dlqData.put("_originalMessageId", messageId);
            dlqData.put("_failedAt", System.currentTimeMillis());
            dlqData.put("_attempts", pending.getTotalDeliveryCount());

            // DLQ 스트림에 추가
            String dlqKey = streamKey + ":dlq";
            redisTemplate.opsForStream().add(dlqKey, dlqData);
            redisTemplate.opsForStream().acknowledge(streamKey, CONSUMER_GROUP, messageId);

        } catch (Exception e) {
            log.error("[PendingRetryScheduler] Failed to move message {} to DLQ", messageId, e);
        }
    }

    private Set<String> findAllChatStreams() {
        try {
            Set<String> keys = redisTemplate.keys(ChatProperties.STREAM_KEY_PREFIX + "*");
            return (keys != null) ? keys : Collections.emptySet();
        } catch (Exception e) {
            log.error("[PendingRetryScheduler] Failed to find chat streams", e);
            return Collections.emptySet();
        }
    }

    private MapRecord<String, String, String> convertToStringRecord(MapRecord<String, Object, Object> record) {
        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : record.getValue().entrySet()) {
            stringMap.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return StreamRecords.newRecord()
                .in(Objects.requireNonNull(record.getStream()))
                .withId(record.getId())
                .ofMap(stringMap);
    }

    private Map<String, Object> copyMapData(Map<Object, Object> source) {
        Map<String, Object> copy = new HashMap<>();
        for (Map.Entry<Object, Object> entry : source.entrySet()) {
            if (entry.getKey() != null) {
                copy.put(entry.getKey().toString(), entry.getValue());
            }
        }
        return copy;
    }
}
