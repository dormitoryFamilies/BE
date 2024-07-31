package dormitoryfamily.doomz.domain.board.article.repository;

import dormitoryfamily.doomz.domain.board.article.dto.request.ArticleRequest;
import dormitoryfamily.doomz.domain.board.article.entity.Article;
import dormitoryfamily.doomz.domain.board.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.board.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ArticleRepositoryCustom {

    Slice<Article> findAllByDormitoryTypeAndBoardType(ArticleDormitoryType dormitoryType, BoardType boardType, ArticleRequest request, Pageable pageable);

    Slice<Article> findMyArticleByDormitoryTypeAndBoardType(Member member, ArticleDormitoryType dormitoryType, BoardType boardType, ArticleRequest request, Pageable pageable);

    Slice<Article> findAllByIdInAndDormitoryTypeAndBoardType(List<Long> articleIds, ArticleDormitoryType dormitoryType, BoardType boardType, ArticleRequest request, Pageable pageable);

    Slice<Article> searchArticles(ArticleDormitoryType dormitoryType, String keyword, Pageable pageable);
}
