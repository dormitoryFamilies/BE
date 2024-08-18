package dormitoryfamily.doomz.domain.notification.dto.request;

import java.util.List;

public record NotificationsSetReadRequestDto(
        List<Long> notificationIds
) {
}
