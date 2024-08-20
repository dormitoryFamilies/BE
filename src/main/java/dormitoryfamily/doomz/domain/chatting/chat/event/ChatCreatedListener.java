package dormitoryfamily.doomz.domain.chatting.chat.event;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatting.chatroom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.notification.entity.type.NotificationType;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatCreatedListener {

    private final NotificationService notificationService;

    @EventListener
    @Async
    public void handleChatCreatedEvent(ChatCreatedEvent event) {
        Chat chat = event.chat();
        ChatRoom chatRoom = chat.getChatRoom();
        Long senderId = chat.getSenderId();

        Member receiver = getReceiver(chatRoom, senderId);
        Member sender = getSender(chatRoom, senderId);

        if (shouldSendNotification(chatRoom, receiver)) {
            sendChatNotification(receiver, sender, event.notificationType(), chatRoom.getId());
        }
    }

    public Member getReceiver(ChatRoom chatRoom, Long senderId) {
        if (chatRoom.getInitiator().getId().equals(senderId)) {
            return chatRoom.getParticipant();
        } else {
            return chatRoom.getInitiator();
        }
    }

    public Member getSender(ChatRoom chatRoom, Long senderId) {
        if (chatRoom.getInitiator().getId().equals(senderId)) {
            return chatRoom.getInitiator();
        } else {
            return chatRoom.getParticipant();
        }
    }

    /**
     * 수신자가 현재 채팅방에 없고, 이미 만들어진 알림이 없음을 검증
     */
    private boolean shouldSendNotification(ChatRoom chatRoom, Member receiver) {
        return chatRoom.isMemberOutOfChatRoom(receiver.getId()) &&
                !notificationService.existsUnreadChatNotificationByReceiver(receiver);
    }

    private void sendChatNotification(Member receiver, Member sender, NotificationType notificationType, Long chatRoomId) {
        notificationService.send(receiver, sender, notificationType, null, chatRoomId);
    }
}
