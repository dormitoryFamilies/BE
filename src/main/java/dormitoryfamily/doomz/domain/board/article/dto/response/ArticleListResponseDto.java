package dormitoryfamily.doomz.domain.board.article.dto.response;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
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
