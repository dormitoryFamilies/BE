package dormitoryfamily.doomz.domain.board.comment.dto.request;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.comment.entity.Comment;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto (
        @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
        String content
){
    public static Comment toEntity(Member member,
                                   Article article,
                                   CreateCommentRequestDto requestDto){
        return Comment.builder()
                .article(article)
                .member(member)
                .content(requestDto.content())
                .isDeleted(false)
                .build();
    }
}

