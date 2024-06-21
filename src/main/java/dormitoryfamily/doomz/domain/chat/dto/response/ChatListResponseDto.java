package dormitoryfamily.doomz.domain.chat.dto.response;

import dormitoryfamily.doomz.domain.chat.dto.ChatDto;


import java.util.List;

public record ChatListResponseDto (
        int nowPageNumber,
        boolean isLast,
        List<ChatDto> ChatHistory
){
    public static ChatListResponseDto toDto(int nowPageNumber, boolean isLast, List<ChatDto> chatDtos){
        return new ChatListResponseDto(
                nowPageNumber,
                isLast,
                chatDtos);
    }

}
