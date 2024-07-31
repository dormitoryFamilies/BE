package dormitoryfamily.doomz.domain.chatting.chatroom.dto.response;

import dormitoryfamily.doomz.domain.chatting.chatroom.entity.ChatRoom;

public record ChatRoomEntryResponseDto(
        Long chatRoomId,
        String roomUUID
) {
    public static ChatRoomEntryResponseDto fromEntity(ChatRoom chatRoom) {
        return new ChatRoomEntryResponseDto(chatRoom.getId(), chatRoom.getRoomUUID());
    }
}