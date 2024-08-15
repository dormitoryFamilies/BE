package dormitoryfamily.doomz.domain.roommate.matching.event.request;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static dormitoryfamily.doomz.domain.notification.entity.type.NotificationType.MATCHING_REQUEST;

@Component
@RequiredArgsConstructor
public class MatchingRequestListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleMatchingRequestEvent(MatchingRequestEvent event) {
        MatchingRequest matchingRequest = event.matchingRequest();
        boolean isRequest = event.notificationType() == MATCHING_REQUEST;

        Member receiver = isRequest ? matchingRequest.getReceiver() : matchingRequest.getSender();
        Member sender = isRequest ? matchingRequest.getSender() : matchingRequest.getReceiver();

        notificationService.send(receiver, sender, event.notificationType(), null, sender.getId());
    }
}
