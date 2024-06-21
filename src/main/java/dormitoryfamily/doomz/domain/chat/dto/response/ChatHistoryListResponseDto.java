package dormitoryfamily.doomz.domain.chat.dto.response;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import org.springframework.data.domain.Slice;

import java.util.List;

public record ChatHistoryListResponseDto(
        int nowPageNumber,
        boolean isLast,
        List<ChatHistoryResponseDto> chatHistory
) {
    public static ChatHistoryListResponseDto toDto(Slice<Chat> chats, List<ChatHistoryResponseDto> chatHistoryResponseDto) {
        return new ChatHistoryListResponseDto(
                chats.getNumber(),
                chats.isLast(),
                chatHistoryResponseDto
        );
    }
}
