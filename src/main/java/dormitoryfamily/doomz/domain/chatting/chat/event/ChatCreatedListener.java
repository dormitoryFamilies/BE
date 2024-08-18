package dormitoryfamily.doomz.domain.chatting.chat.event;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatting.chatroom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
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

        if(chatRoom.isMemberOutOfChatRoom(receiver.getId())) {
            notificationService.send(receiver, sender, event.notificationType(), null, chatRoom.getId());
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
}
