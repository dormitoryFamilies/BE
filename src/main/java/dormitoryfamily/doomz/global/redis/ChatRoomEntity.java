package dormitoryfamily.doomz.global.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatRoomEntity(
        Long id,
        String roomUUID,
        Long senderId,
        int receiverUnreadCount,
        boolean senderIsDeleted,
        Long receiverId,
        int senderUnreadCount,
        boolean receiverIsDeleted,
        String latestText) implements Serializable {

    public static ChatRoomEntity create(ChatRoom chatRoom){
        return new ChatRoomEntity(
                chatRoom.getId(),
                chatRoom.getRoomUUID(),
                chatRoom.getSender().getId(),
                chatRoom.getSenderUnreadCount(),
                chatRoom.isSenderIsDeleted(),
                chatRoom.getReceiver().getId(),
                chatRoom.getReceiverUnreadCount(),
                chatRoom.isReceiverIsDeleted(),
                null
        );
    }
}
