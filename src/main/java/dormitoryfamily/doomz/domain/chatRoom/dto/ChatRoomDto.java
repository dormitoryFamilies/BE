package dormitoryfamily.doomz.domain.chatRoom.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomDto implements Serializable {

    private Long id;
    private String roomUUID;
    private Long senderId;
    private Long receiverId;
    private String chatRoomStatus;
    private int senderUnreadCount;
    private int receiverUnreadCount;
    private Long lastReceiverOnlyChatId;
    private Long lastSenderOnlyChatId;
    private String senderStatus;
    private String receiverStatus;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom) {
        return new ChatRoomDto(
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