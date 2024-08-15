package dormitoryfamily.doomz.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.notification.entity.Notification;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long notificationId,
        String type,
        String sender,
        String articleTitle,
        boolean isRead,
        Long targetId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getNotificationType().toString(),
                notification.getSender().getNickname(),
                notification.getArticleTitle(),
                notification.isRead(),
                notification.getTargetId(),
                notification.getCreatedAt()
        );
    }
}
