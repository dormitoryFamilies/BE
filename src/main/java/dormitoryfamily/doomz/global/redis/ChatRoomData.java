package dormitoryfamily.doomz.global.redis;

import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;

import static ch.qos.logback.core.joran.JoranConstants.NULL;

public record ChatRoomData (
        Long id,
        String roomUUID,
        Long senderId,
        int receiverUnreadCount,
        boolean senderIsDeleted,
        Long receiverId,
        int senderUnreadCount,
        boolean receiverIsDeleted,
        String latestText){

    public static ChatRoomData create(ChatRoom chatRoom){
        return new ChatRoomData(
                chatRoom.getId(),
                chatRoom.getRoomUUID(),
                chatRoom.getSender().getId(),
                chatRoom.getSenderUnreadCount(),
                chatRoom.isSenderIsDeleted(),
                chatRoom.getReceiver().getId(),
                chatRoom.getReceiverUnreadCount(),
                chatRoom.isReceiverIsDeleted(),
                NULL
        );
    }
}
