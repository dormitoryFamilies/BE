package dormitoryfamily.doomz.domain.roommate.event;

import dormitoryfamily.doomz.global.elasticsearch.ElasticScriptQueryExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchIndexEventListener {

    private final ElasticScriptQueryExecutor elasticScriptQueryExecutor;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLifestyleIndexEvent(LifestyleIndexEvent event) {
        try {
            elasticScriptQueryExecutor.indexLifestyleVector(event.member(), event.lifestyle());
            log.info("라이프스타일 ES 인덱싱 완료: memberId={}", event.member().getId());
        } catch (Exception e) {
            log.error("라이프스타일 ES 인덱싱 실패: memberId={}, error={}",
                    event.member().getId(), e.getMessage(), e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePreferenceIndexEvent(PreferenceIndexEvent event) {
        try {
            elasticScriptQueryExecutor.indexPreferenceVector(
                    event.memberId(),
                    event.preferenceOrder(),
                    event.member()
            );
            log.info("선호도 ES 인덱싱 완료: memberId={}", event.memberId());
        } catch (Exception e) {
            log.error("선호도 ES 인덱싱 실패: memberId={}, error={}",
                    event.memberId(), e.getMessage(), e);
        }
    }
}
