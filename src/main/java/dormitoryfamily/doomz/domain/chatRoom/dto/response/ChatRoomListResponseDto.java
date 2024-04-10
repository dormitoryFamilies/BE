package dormitoryfamily.doomz.domain.chatRoom.dto.response;

import java.util.List;

public record ChatRoomListResponseDto(
        List<ChatRoomResponseDto> chatRoomResponseDtos
) {
    public static ChatRoomListResponseDto toDto(List<ChatRoomResponseDto> chatRoomResponseDtos){
        return new ChatRoomListResponseDto(chatRoomResponseDtos);
    }
}
