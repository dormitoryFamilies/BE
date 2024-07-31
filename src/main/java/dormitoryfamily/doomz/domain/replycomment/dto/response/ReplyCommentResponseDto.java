package dormitoryfamily.doomz.domain.replycomment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replycomment.entity.ReplyComment;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReplyCommentResponseDto(
        Long replyCommentId,
        Long memberId,
        String profileUrl,
        String nickname,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        String content,
        boolean isArticleWriter
) {
    public static ReplyCommentResponseDto fromEntity(Member articleWriter, ReplyComment replyComment) {
        return new ReplyCommentResponseDto(
                replyComment.getId(),
                replyComment.getMember().getId(),
                replyComment.getMember().getProfileUrl(),
                replyComment.getMember().getNickname(),
                replyComment.getCreatedAt(),
                replyComment.getContent(),
                isArticleWriter(articleWriter, replyComment.getMember())
        );
    }

    private static boolean isArticleWriter(Member articleWriter, Member replyCommentWriter) {
        return Objects.equals(articleWriter.getId(), replyCommentWriter.getId());
    }
}



