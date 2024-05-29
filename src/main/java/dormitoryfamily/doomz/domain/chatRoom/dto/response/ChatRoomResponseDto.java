package dormitoryfamily.doomz.domain.chatRoom.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.time.LocalDateTime;

public record ChatRoomResponseDto (

        Long roomId,
        String roomUUID,
        Long memberId,
        String memberNickName,
        String memberProfileUrl,
        int unReadCount,
        String lastMessage,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastMessageTime
        ){

        public static ChatRoomResponseDto fromEntity(ChatRoom chatRoom, Chat lastChat, boolean isSender) {
                Member member = isSender ? chatRoom.getReceiver() : chatRoom.getSender();
                int unreadCount = isSender ? chatRoom.getSenderUnreadCount() : chatRoom.getReceiverUnreadCount();

                return new ChatRoomResponseDto(
                        chatRoom.getId(),
                        chatRoom.getRoomUUID(),
                        member.getId(),
                        member.getNickname(),
                        member.getProfileUrl(),
                        unreadCount,
                        lastChat.getMessage() != null ? lastChat.getMessage() : lastChat.getImageUrl(),
                        lastChat.getCreatedAt()
                );
        }
}
