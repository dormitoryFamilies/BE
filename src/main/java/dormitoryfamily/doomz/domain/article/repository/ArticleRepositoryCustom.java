package dormitoryfamily.doomz.domain.article.repository;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ArticleRepositoryCustom {

    Slice<Article> findAllByDormitoryTypeAndBoardType(ArticleDormitoryType dormitoryType, BoardType boardType, ArticleRequest request, Pageable pageable);
    Slice<Article> searchArticles(ArticleDormitoryType dormitoryType, String keyword, Pageable pageable);
}
