package dormitoryfamily.doomz.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Repository
public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String eventCacheId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithEmail(String memberEmail);
    Map<String, Object> findAllEventCacheStartWithEmail(String memberEmail);
    void deleteById(String emitterId);

}
