package dormitoryfamily.doomz.domain.roommate.wish.event;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import dormitoryfamily.doomz.domain.roommate.wish.entity.RoommateWish;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoommateWishCreatedListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleCommentCreatedEvent(RoommateWishCreatedEvent event) {
        RoommateWish roommateWish = event.roommateWish();
        Member wished = roommateWish.getWished();
        Member wisher = roommateWish.getWisher();

        notificationService.send(wished, wisher, event.notificationType(), null, wisher.getId());
    }
}
