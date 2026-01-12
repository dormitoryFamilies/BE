package dormitoryfamily.doomz.global.chat;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatMetrics {

    private final Counter dlqMovedCounter;
    private final Counter messageRetryCounter;
    private final Counter messageProcessSuccessCounter;
    private final Counter messageProcessFailureCounter;

    public ChatMetrics(MeterRegistry meterRegistry) {
        // DLQ로 이동된 메시지 수
        this.dlqMovedCounter = Counter.builder("chat.message.dlq.moved")
                .description("Number of messages moved to DLQ")
                .tag("type", "chat")
                .register(meterRegistry);

        // 재시도된 메시지 수
        this.messageRetryCounter = Counter.builder("chat.message.retry")
                .description("Number of messages retried from pending")
                .tag("type", "chat")
                .register(meterRegistry);

        // 메시지 처리 성공 수
        this.messageProcessSuccessCounter = Counter.builder("chat.message.process.success")
                .description("Number of successfully processed messages")
                .tag("type", "chat")
                .register(meterRegistry);

        // 메시지 처리 실패 수
        this.messageProcessFailureCounter = Counter.builder("chat.message.process.failure")
                .description("Number of failed message processing")
                .tag("type", "chat")
                .register(meterRegistry);
    }

    /**
     * DLQ로 메시지가 이동되었을 때 호출
     */
    public void incrementDlqMoved(String streamKey) {
        dlqMovedCounter.increment();
        log.warn("[ChatMetrics] Message moved to DLQ - stream: {}", streamKey);
    }

    /**
     * 메시지가 재시도되었을 때 호출
     */
    public void incrementRetry(String streamKey, int count) {
        messageRetryCounter.increment(count);
        log.info("[ChatMetrics] Messages retried - stream: {}, count: {}", streamKey, count);
    }

    /**
     * 메시지 처리 성공 시 호출
     */
    public void incrementProcessSuccess() {
        messageProcessSuccessCounter.increment();
    }

    /**
     * 메시지 처리 실패 시 호출
     */
    public void incrementProcessFailure() {
        messageProcessFailureCounter.increment();
    }
}
