package dormitoryfamily.doomz.domain.board.article.dto.response;

import dormitoryfamily.doomz.domain.board.article.entity.Article;

public record CreateArticleResponseDto(
        Long articleId
) {
    public static CreateArticleResponseDto fromEntity(Article article) {
        return new CreateArticleResponseDto(article.getId());
    }
}
