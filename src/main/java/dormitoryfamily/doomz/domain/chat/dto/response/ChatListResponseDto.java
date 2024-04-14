package dormitoryfamily.doomz.domain.chat.dto.response;


import dormitoryfamily.doomz.domain.chat.dto.ChatDto;

import java.util.List;

public record ChatListResponseDto (
        List<ChatDto> ChatHistory
){
    public static ChatListResponseDto toDto(List<ChatDto> chatDtos){
        return new ChatListResponseDto(chatDtos);
    }
}
