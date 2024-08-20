package dormitoryfamily.doomz.domain.roommate.matching.event.request;

import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingRequest;

public record MatchingRequestEvent(
        MatchingRequest matchingRequest,
        NotificationType notificationType
) {
}
