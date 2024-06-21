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
        String chatRoomStatus,
        int senderUnreadCount,
        int receiverUnreadCount,
        Long lastReceiverOnlyChatId,
        Long lastSenderOnlyChatId,
        String senderStatus,
        String receiverStatus) implements Serializable
{
    public static ChatRoomEntity fromEntity(ChatRoom chatRoom){
        return new ChatRoomEntity(
                chatRoom.getId(),
                chatRoom.getRoomUUID(),
                chatRoom.getSender().getId(),
                chatRoom.getReceiver().getId(),
                chatRoom.getChatRoomStatus().toString(),
                chatRoom.getSenderUnreadCount(),
                chatRoom.getReceiverUnreadCount(),
                chatRoom.getLastReceiverOnlyChatId(),
                chatRoom.getLastSenderOnlyChatId(),
                chatRoom.getSenderStatus().toString(),
                chatRoom.getReceiverStatus().toString()
        );
    }
}
