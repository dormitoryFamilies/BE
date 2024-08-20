package dormitoryfamily.doomz.domain.roommate.matching.event.result;

import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.domain.roommate.matching.entity.MatchingResult;

public record MatchingResultEvent(
        MatchingResult matchingResult,
        NotificationType notificationType
) {
}
