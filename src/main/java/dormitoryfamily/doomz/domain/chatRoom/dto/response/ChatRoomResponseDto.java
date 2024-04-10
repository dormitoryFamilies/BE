package dormitoryfamily.doomz.domain.chatRoom.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomResponseDto (

        Long roomId,
        String roomUUID,
        Long chatMemberId,
        String chatMemberName,
        int unReadCount,
        String lastMessage,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastMessageTime
        ){

        public static ChatRoomResponseDto fromEntityWhenSender(ChatRoom chatRoom, Chat lastChat){
                return new ChatRoomResponseDto(
                        chatRoom.getId(),
                        chatRoom.getRoomUUID(),
                        chatRoom.getReceiver().getId(),
                        chatRoom.getReceiver().getName(),
                        chatRoom.getSenderUnreadCount(),
                        lastChat.getMessage(),
                        lastChat.getCreatedAt()
                );
        }

        public static ChatRoomResponseDto fromEntityWhenReceiver(ChatRoom chatRoom, Chat lastChat){
                return new ChatRoomResponseDto(
                        chatRoom.getId(),
                        chatRoom.getRoomUUID(),
                        chatRoom.getSender().getId(),
                        chatRoom.getSender().getName(),
                        chatRoom.getReceiverUnreadCount(),
                        lastChat.getMessage(),
                        lastChat.getCreatedAt()
                );
        }
}