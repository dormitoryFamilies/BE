package dormitoryfamily.doomz.domain.chatting.chat.dto.response;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import org.springframework.data.domain.Slice;

import java.util.List;

public record SearchChatListResponseDto(
        int nowPageNumber,
        boolean isLast,
        List<SearchChatResponseDto> chatHistory
) {
    public static SearchChatListResponseDto from(Slice<Chat> chats, List<SearchChatResponseDto> SearchChatResponseDto) {
        return new SearchChatListResponseDto(
                chats.getNumber(),
                chats.isLast(),
                SearchChatResponseDto
        );
    }
}
