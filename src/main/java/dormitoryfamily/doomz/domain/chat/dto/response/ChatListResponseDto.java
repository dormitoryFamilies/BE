package dormitoryfamily.doomz.domain.chat.dto.response;

import dormitoryfamily.doomz.domain.chat.dto.ChatDto;

import java.util.List;

public record ChatListResponseDto(
        int nowPageNumber,
        boolean isLast,
        String roomUUID,
        List<ChatResponseDto> chatHistory
) {
    public static ChatListResponseDto toDto(int nowPageNumber,
                                            boolean isLast,
                                            String roomUUID,
                                            List<ChatResponseDto> chatResponseDtos
    ) {
        return new ChatListResponseDto(
                nowPageNumber,
                isLast,
                roomUUID,
                chatResponseDtos);
    }
}
