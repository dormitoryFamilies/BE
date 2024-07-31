package dormitoryfamily.doomz.domain.chatting.chatroom.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chatting.chatroom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.member.member.entity.Member;

import java.time.LocalDateTime;

public record ChatRoomResponseDto (

        Long roomId,
        Long memberId,
        String memberNickname,
        String memberProfileUrl,
        int unReadCount,
        String lastMessage,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime lastMessageTime
        ){

        public static ChatRoomResponseDto fromEntity(ChatRoom chatRoom, Chat lastChat, boolean isInitiator) {
                Member member = isInitiator ? chatRoom.getParticipant() : chatRoom.getInitiator();
                int unreadCount = isInitiator ? chatRoom.getInitiatorUnreadCount() : chatRoom.getParticipantUnreadCount();

                return new ChatRoomResponseDto(
                        chatRoom.getId(),
                        member.getId(),
                        member.getNickname(),
                        member.getProfileUrl(),
                        unreadCount,
                        lastChat.getMessage(),
                        lastChat.getCreatedAt()
                );
        }
}
