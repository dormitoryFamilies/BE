package dormitoryfamily.doomz.domain.article.repository;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ArticleRepositoryCustom {

    //    @EntityGraph(attributePaths = "member")
    Slice<Article> findAllByDormitoryType(ArticleDormitoryType dormitoryType, ArticleRequest request, Pageable pageable);
    Slice<Article> searchArticles(ArticleDormitoryType dormitoryType, String keyword, Pageable pageable);
}
