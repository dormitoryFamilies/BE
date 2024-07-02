package dormitoryfamily.doomz.domain.article.dto.response;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Slice;

import java.util.List;

public record ArticleListResponseDto(
        long loginMemberId,
        int nowPageNumber,
        boolean isLast,
        List<SimpleArticleResponseDto> articles
) {
    public static ArticleListResponseDto fromResponseDtos(
            Member loginMember,
            Slice<Article> articles,
            List<SimpleArticleResponseDto> articleResponseDtos
    ) {
        return new ArticleListResponseDto(
                loginMember.getId(),
                articles.getNumber(),
                articles.isLast(),
                articleResponseDtos
        );
    }
}
