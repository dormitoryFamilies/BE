package dormitoryfamily.doomz.domain.roommate.wish.event;

import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.domain.roommate.wish.entity.RoommateWish;

public record RoommateWishCreatedEvent(
        RoommateWish roommateWish,
        NotificationType notificationType
) {
}
