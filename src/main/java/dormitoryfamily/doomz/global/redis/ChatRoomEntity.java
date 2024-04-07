package dormitoryfamily.doomz.global.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatRoomEntity(
        Long id,
        String roomUUID,
        Long senderId,
        Long receiverId,
        int senderUnreadCount,
        int receiverUnreadCount,
        String chatRoomStatus,
        String latestText) implements Serializable {

    public static ChatRoomEntity create(ChatRoom chatRoom){
        return new ChatRoomEntity(
                chatRoom.getId(),
                chatRoom.getRoomUUID(),
                chatRoom.getSender().getId(),
                chatRoom.getReceiver().getId(),
                chatRoom.getSenderUnreadCount(),
                chatRoom.getReceiverUnreadCount(),
                chatRoom.getChatRoomStatus().toString(),
                null
        );
    }
}
