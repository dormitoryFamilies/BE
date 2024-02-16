package dormitoryfamily.doomz.domain.article.dto.response;

import dormitoryfamily.doomz.domain.article.entity.Article;

public record CreateArticleResponseDto(
        Long articleId
) {
    public static CreateArticleResponseDto fromEntity(Article article) {
        return new CreateArticleResponseDto(article.getId());
    }
}
