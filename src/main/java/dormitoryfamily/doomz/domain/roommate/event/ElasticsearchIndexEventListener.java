package dormitoryfamily.doomz.domain.roommate.event;

import dormitoryfamily.doomz.global.elasticsearch.ElasticScriptQueryExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;

@Slf4j
@Component
public class ElasticsearchIndexEventListener {

    private final ElasticScriptQueryExecutor elasticScriptQueryExecutor;
    private final TaskScheduler retryTaskScheduler;

    public ElasticsearchIndexEventListener(
            ElasticScriptQueryExecutor elasticScriptQueryExecutor,
            @Qualifier("retryTaskScheduler") TaskScheduler retryTaskScheduler
    ) {
        this.elasticScriptQueryExecutor = elasticScriptQueryExecutor;
        this.retryTaskScheduler = retryTaskScheduler;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLifestyleIndexEvent(LifestyleIndexEvent event) {
        scheduleWithRetry(
                () -> elasticScriptQueryExecutor.indexLifestyleVector(event.member(), event.lifestyle()),
                "라이프스타일",
                event.member().getId(),
                0
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePreferenceIndexEvent(PreferenceIndexEvent event) {
        scheduleWithRetry(
                () -> elasticScriptQueryExecutor.indexPreferenceVector(
                        event.memberId(),
                        event.preferenceOrder(),
                        event.member()
                ),
                "선호도",
                event.memberId(),
                0
        );
    }

    private void scheduleWithRetry(Runnable task, String taskName, Long memberId, int attempt) {
        retryTaskScheduler.schedule(() -> {
            try {
                task.run();
                log.info("{} ES 인덱싱 성공: memberId={}, attempt={}", taskName, memberId, attempt);
            } catch (Exception e) {
                if (attempt >= 1) {
                    log.error("{} ES 인덱싱 최종 실패: memberId={}", taskName, memberId, e);
                    return;
                }
                log.warn("{} ES 인덱싱 실패, 재시도 예약: memberId={}, attempt={}", taskName, memberId, attempt, e);
                scheduleWithRetry(task, taskName, memberId, attempt + 1);
            }
        }, Instant.now().plusSeconds(attempt == 0 ? 0 : 1));
    }
}
