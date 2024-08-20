package dormitoryfamily.doomz.domain.member.follow.event;

import dormitoryfamily.doomz.domain.member.follow.entity.Follow;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;

public record FollowCreatedEvent(
        Follow follow,
        NotificationType notificationType
) {
}
