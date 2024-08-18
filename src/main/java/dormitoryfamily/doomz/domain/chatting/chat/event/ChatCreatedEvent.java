package dormitoryfamily.doomz.domain.chatting.chat.event;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;

public record ChatCreatedEvent(
        Chat chat,
        NotificationType notificationType
) {
}
