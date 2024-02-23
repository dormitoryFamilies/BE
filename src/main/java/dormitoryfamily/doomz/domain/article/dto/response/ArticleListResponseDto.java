package dormitoryfamily.doomz.domain.article.dto.response;

import dormitoryfamily.doomz.domain.article.entity.Article;
import org.springframework.data.domain.Slice;

import java.util.List;

public record ArticleListResponseDto(
    int nowPageNumber,
    boolean isLast,
    List<SimpleArticleResponseDto> articles
) {
    public static ArticleListResponseDto fromResponseDtos(
            Slice<Article> articles,
            List<SimpleArticleResponseDto> articleResponseDtos
    ) {
        return new ArticleListResponseDto(
                articles.getNumber(),
                articles.isLast(),
                articleResponseDtos
        );
    }
}
