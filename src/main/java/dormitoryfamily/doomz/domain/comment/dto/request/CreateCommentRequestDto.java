package dormitoryfamily.doomz.domain.comment.dto.request;


import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.comment.entity.Comment;
import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequestDto (
        @NotNull(message = "댓글 내용은 null일 수 없습니다.")
        String content
){
    public static Comment toComment(Member member, Article article, CreateCommentRequestDto requestDto){
        return Comment.builder()
                .article(article)
                .member(member)
                .content(requestDto.content())
                .isDeleted(false)
                .build();
    }

    public static Comment toChildComment(Member member, Comment parentComment, CreateCommentRequestDto requestDto){
        return Comment.builder()
                .article(parentComment.getArticle())
                .member(member)
                .parentComment(parentComment)
                .content(requestDto.content())
                .isDeleted(false)
                .build();
    }
}

