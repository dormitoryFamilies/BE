package dormitoryfamily.doomz.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.member.entity.Member;

import java.time.LocalDateTime;

public record SearchChatResponseDto(
        Long roomId,
        Long memberId,
        String memberNickname,
        String memberProfileUrl,
        String chatMessage,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime sentTime
) {
    public static SearchChatResponseDto fromEntity(Chat chat, Member member) {
        return new SearchChatResponseDto(
                chat.getChatRoom().getId(),
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                chat.getMessage(),
                chat.getCreatedAt()
        );
    }
}
