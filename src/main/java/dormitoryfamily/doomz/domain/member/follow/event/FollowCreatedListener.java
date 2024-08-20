package dormitoryfamily.doomz.domain.member.follow.event;

import dormitoryfamily.doomz.domain.member.follow.entity.Follow;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowCreatedListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleCommentCreatedEvent(FollowCreatedEvent event) {
        Follow follow = event.follow();

        notificationService.send(follow.getFollowing(), follow.getFollower(), event.notificationType(), null, follow.getFollower().getId());
    }
}
