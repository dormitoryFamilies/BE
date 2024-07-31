package dormitoryfamily.doomz.domain.chatroom.dto.response;

import dormitoryfamily.doomz.domain.chatroom.entity.ChatRoom;

public record ChatRoomIdResponseDto (
        Long roomId
){
    public static ChatRoomIdResponseDto fromEntity(ChatRoom chatRoom){
        return new ChatRoomIdResponseDto(chatRoom.getId());
    }
}
