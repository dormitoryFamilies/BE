package dormitoryfamily.doomz.domain.chatRoom.dto.response;

import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;

public record ChatRoomIdResponseDto (
        Long roomId
){
    public static ChatRoomIdResponseDto fromEntity(ChatRoom chatRoom){
        return new ChatRoomIdResponseDto(chatRoom.getId());
    }
}
