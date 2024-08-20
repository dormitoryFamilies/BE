package dormitoryfamily.doomz.domain.roommate.matching.event.result;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingResultListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleMatchingResultEvent(MatchingResultEvent event) {
        MatchingResult matchingResult = event.matchingResult();
        Member receiver = matchingResult.getReceiver();
        Member sender = matchingResult.getSender();

        notificationService.send(receiver, sender, event.notificationType(), null, sender.getId());
    }
}
