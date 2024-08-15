package dormitoryfamily.doomz.domain.notification.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.dto.NotificationResponseDto;
import dormitoryfamily.doomz.domain.notification.entity.Notification;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.domain.notification.repository.EmitterRepository;
import dormitoryfamily.doomz.domain.notification.repository.NotificationRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import static dormitoryfamily.doomz.domain.notification.util.NotificationProperties.*;

//todo. 2개월 이상 된 알림 삭제하기
//todo. 전체 알림 조회
//todo. 알림 특정 알림 읽음 표시
//todo. 전체 읽음 처리
//todo. 확인 사항 4. SSE 저장, 이벤트 저장을 전체 조회하는 기능이 진짜 필요한지 판단하기

//todo. 확인 사항 1. 다른 사람이 행동한 것도 잘 동작하는지
//todo. 확인 사항 2. 내 스스로 댓글, 대댓글, 찜하기 한 건 알림 안가기
//todo. 확인 사항 3. 전체 알림 조회 잘 되는지
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(PrincipalDetails principalDetails, String lastEventId) {
        Member loginMember = principalDetails.getMember();
        String emitterId = makeIdIncludedMemberEmail(loginMember.getEmail());

        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        sendDummyEventFor503Error(loginMember, emitter, emitterId);

        if (hasLostEvents(lastEventId)) {
            sendLostEvents(lastEventId, loginMember, emitter, emitterId);
        }

        return emitter;
    }

    private void sendLostEvents(String lastEventId, Member loginMember, SseEmitter emitter, String emitterId) {
        Map<String, Object> leftEventCaches = emitterRepository.findAllEventCacheStartWithEmail(loginMember.getEmail());
        leftEventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    private static boolean hasLostEvents(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendDummyEventFor503Error(Member loginMember, SseEmitter emitter, String emitterId) {
        String eventId = makeIdIncludedMemberEmail(loginMember.getEmail());
        sendNotification(emitter, eventId, emitterId, "eventStream Created, [userEmail=" + loginMember.getEmail() + "]");
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name(EVENT_NAME)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private static String makeIdIncludedMemberEmail(String memberEmail) {
        return memberEmail + SEPARATOR + System.currentTimeMillis();
    }

    public void send(Member receiver, Member sender, NotificationType notificationType, String articleTitle, Long targetId) {
        Notification notification = notificationRepository.save(createNotification(receiver, sender, notificationType, articleTitle, targetId));

        String receiverEmail = receiver.getEmail();
        String eventId = makeIdIncludedMemberEmail(receiverEmail);
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithEmail(receiverEmail);
        emitters.forEach(
                (emitterId, emitter) -> {
                    emitterRepository.saveEventCache(emitterId, notification);
                    sendNotification(emitter, eventId, emitterId, NotificationResponseDto.from(notification));
                }
        );
    }

    private Notification createNotification(Member receiver, Member sender, NotificationType notificationType, String articleTitle, Long targetId) {
        return Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .notificationType(notificationType)
                .isRead(false)
                .articleTitle(articleTitle)
                .targetId(targetId)
                .build();
    }
}
