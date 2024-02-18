package dormitoryfamily.doomz.domain.replyComment.dto.request;

import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.replyComment.entity.ReplyComment;
import jakarta.validation.constraints.NotNull;

public record CreateReplyCommentRequestDto(
        @NotNull(message = "대댓글 내용은 null일 수 없습니다.")
        String content
){
    public static ReplyComment toEntity(Member member, Comment comment, CreateReplyCommentRequestDto requestDto){
        return ReplyComment.builder()
                .member(member)
                .comment(comment)
                .content(requestDto.content)
                .build();
    }
}

