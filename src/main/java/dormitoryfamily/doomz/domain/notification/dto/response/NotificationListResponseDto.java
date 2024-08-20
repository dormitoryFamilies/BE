package dormitoryfamily.doomz.domain.notification.dto.response;

import java.util.List;

public record NotificationListResponseDto(

        boolean isLast,
        List<NotificationResponseDto> notifications

) {
    public static NotificationListResponseDto from(boolean isLast, List<NotificationResponseDto> notifications) {
        return new NotificationListResponseDto(isLast, notifications);
    }
}
