package dormitoryfamily.doomz.domain.chatRoom.dto.response;

import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;

public record CreateChatRoomResponseDto(
        Long chatRoomId,
        String roomUUID
) {
    public static CreateChatRoomResponseDto fromEntity(ChatRoom chatRoom) {
        return new CreateChatRoomResponseDto(chatRoom.getId(), chatRoom.getRoomUUID());
    }
}