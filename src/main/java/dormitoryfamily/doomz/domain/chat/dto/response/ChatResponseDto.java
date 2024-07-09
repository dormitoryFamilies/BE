package dormitoryfamily.doomz.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.time.LocalDateTime;

public record ChatResponseDto(
        Long memberId,
        boolean isSender,
        String memberNickname,
        String memberProfileUrl,
        String chatMessage,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime sentTime
) {
    public static ChatResponseDto fromChatDto(ChatDto chatDto, Member chatMember, boolean isChatSender) {
        return new ChatResponseDto(
                chatMember.getId(),
                isChatSender,
                chatMember.getNickname(),
                chatMember.getProfileUrl(),
                chatDto.message(),
                chatDto.sentTime()
        );
    }

}
