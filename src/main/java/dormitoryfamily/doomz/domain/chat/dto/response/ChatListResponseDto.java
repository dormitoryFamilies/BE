package dormitoryfamily.doomz.domain.chat.dto.response;

import dormitoryfamily.doomz.domain.chat.dto.ChatDto;


import java.util.List;

public record ChatListResponseDto (
        int nowPageNumber,
        boolean isLast,
        String roomUUID,
        List<ChatDto> chatHistory
){
    public static ChatListResponseDto toDto(int nowPageNumber,
                                            boolean isLast,
                                            String roomUUID,
                                            List<ChatDto> chatDtos
    ){
        return new ChatListResponseDto(
                nowPageNumber,
                isLast,
                roomUUID,
                chatDtos);
    }
}
