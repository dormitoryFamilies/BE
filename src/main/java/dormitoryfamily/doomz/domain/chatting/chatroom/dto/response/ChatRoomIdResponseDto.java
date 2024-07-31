package dormitoryfamily.doomz.domain.chatting.chatroom.dto.response;

import dormitoryfamily.doomz.domain.chatting.chatroom.entity.ChatRoom;

public record ChatRoomIdResponseDto (
        Long roomId
){
    public static ChatRoomIdResponseDto fromEntity(ChatRoom chatRoom){
        return new ChatRoomIdResponseDto(chatRoom.getId());
    }
}
