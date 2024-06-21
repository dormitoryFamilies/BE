package dormitoryfamily.doomz.domain.replyComment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReplyCommentResponseDto(
        Long replyCommentId,
        Long memberId,
        String profileUrl,
        String nickname,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yryyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        String content,
        boolean isWriter
) {
    public static ReplyCommentResponseDto fromEntity(Member loginMember, ReplyComment replyComment) {
        return new ReplyCommentResponseDto(
                replyComment.getId(),
                replyComment.getMember().getId(),
                replyComment.getMember().getProfileUrl(),
                replyComment.getMember().getNickname(),
                replyComment.getCreatedAt(),
                replyComment.getContent(),
                isArticleWriter(loginMember, replyComment.getComment().getArticle().getMember())
        );
    }

    private static boolean isArticleWriter(Member loginMember, Member articleWriter) {
        return Objects.equals(loginMember.getId(), articleWriter.getId());
    }
}



