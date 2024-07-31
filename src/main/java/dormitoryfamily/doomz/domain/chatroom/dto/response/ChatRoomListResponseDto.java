package dormitoryfamily.doomz.domain.chatroom.dto.response;

import dormitoryfamily.doomz.domain.chatroom.entity.ChatRoom;
import org.springframework.data.domain.Slice;

import java.util.List;

public record ChatRoomListResponseDto(
        int nowPageNumber,
        boolean isLast,
        List<ChatRoomResponseDto> chatRooms
) {
    public static ChatRoomListResponseDto from(Slice<ChatRoom> chatRooms,
                                                List<ChatRoomResponseDto> chatRoomResponseDtos){
        return new ChatRoomListResponseDto(
                chatRooms.getNumber(),
                chatRooms.isLast(),
                chatRoomResponseDtos);
    }
}
