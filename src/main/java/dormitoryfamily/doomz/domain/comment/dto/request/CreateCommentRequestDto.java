package dormitoryfamily.doomz.domain.comment.dto.request;


import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequestDto (
        @NotNull(message = "댓글 내용은 null일 수 없습니다.")
        String content
){
    public static Comment toEntity(Member member, Article article, CreateCommentRequestDto requestDto){
        return Comment.builder()
                .article(article)
                .member(member)
                .content(requestDto.content())
                .isDeleted(false)
                .build();
    }
}

