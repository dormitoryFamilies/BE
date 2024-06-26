package dormitoryfamily.doomz.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.dto.response.ReplyCommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public record CommentResponseDto (
        Long commentId,
        Long memberId,
        String profileUrl,
        String nickname,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        String content,
        boolean isArticleWriter,
        boolean isDeleted,
        List<ReplyCommentResponseDto> replyComments
){
    public static CommentResponseDto fromEntity(Member loginMember, Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getMember().getProfileUrl(),
                comment.getMember().getNickname(),
                comment.getCreatedAt(),
                comment.getContent(),
                isArticleWriter(loginMember, comment.getArticle().getMember()),
                comment.isDeleted(),
                comment.getReplyComments().stream()
                        .map(replyComment -> ReplyCommentResponseDto.fromEntity(loginMember, replyComment))
                        .collect(toList())
        );
    }

    private static boolean isArticleWriter(Member loginMember, Member ArticleWriter) {
        return Objects.equals(loginMember.getId(), ArticleWriter.getId());
    }
}
