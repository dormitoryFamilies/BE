package dormitoryfamily.doomz.domain.notification.service;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.dto.request.NotificationsSetReadRequestDto;
import dormitoryfamily.doomz.domain.notification.dto.response.NotificationListResponseDto;
import dormitoryfamily.doomz.domain.notification.dto.response.NotificationResponseDto;
import dormitoryfamily.doomz.domain.notification.entity.Notification;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.domain.notification.exception.NotMyNotificationException;
import dormitoryfamily.doomz.domain.notification.exception.NotificationAlreadyReadException;
import dormitoryfamily.doomz.domain.notification.exception.NotificationNotExistsException;
import dormitoryfamily.doomz.domain.notification.repository.EmitterRepository;
import dormitoryfamily.doomz.domain.notification.repository.NotificationRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static dormitoryfamily.doomz.domain.notification.util.NotificationProperties.*;

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
                    sendNotification(emitter, eventId, emitterId, NEW_NOTIFICATION_CREATED);
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

    public NotificationListResponseDto getAllNotifications(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        Page<Notification> notifications = notificationRepository.findAllByReceiver(loginMember, pageable);

        return NotificationListResponseDto.from(
                notifications.isLast(),
                notifications.stream()
                        .map(NotificationResponseDto::from)
                        .toList());
    }

    @Transactional
    public void setNotificationAsRead(PrincipalDetails principalDetails, NotificationsSetReadRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        requestDto.notificationIds().forEach(id -> {
            Notification notification = getNotificationById(id);
            validateNotification(notification, loginMember);
            markNotificationAsRead(notification);
        });
    }

    private Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotExistsException(id));
    }

    private static void validateNotification(Notification notification, Member loginMember) {
        if (notification.isRead()) {
            throw new NotificationAlreadyReadException(notification.getId());
        }
        if (!Objects.equals(notification.getReceiver().getId(), loginMember.getId())) {
            throw new NotMyNotificationException(notification.getId());
        }
    }

    private void markNotificationAsRead(Notification notification) {
        notification.setReadStatusToTrue();
    }

    public boolean existsUnreadChatNotificationByReceiver(Member receiver) {
        return notificationRepository.existsUnreadChatNotification(receiver.getId());
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void deleteOldNotifications() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusDays(DELETE_AFTER_DAYS);
        notificationRepository.deleteByCreatedAtBefore(oneMonthAgo);
    }

    @Transactional
    public void deleteNotificationByTypeAndTargetIdAndReceiverId(NotificationType notificationType, Long targetId, Long receiverId) {
        notificationRepository.deleteByNotificationTypeChatAndTargetIdAndReceiverId(notificationType, targetId, receiverId);
    }
}
