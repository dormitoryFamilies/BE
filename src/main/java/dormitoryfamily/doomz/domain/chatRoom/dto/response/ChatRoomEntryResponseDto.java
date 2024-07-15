package dormitoryfamily.doomz.domain.chatRoom.dto.response;

import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;

public record ChatRoomEntryResponseDto(
        Long chatRoomId,
        String roomUUID
) {
    public static ChatRoomEntryResponseDto fromEntity(ChatRoom chatRoom) {
        return new ChatRoomEntryResponseDto(chatRoom.getId(), chatRoom.getRoomUUID());
    }
}