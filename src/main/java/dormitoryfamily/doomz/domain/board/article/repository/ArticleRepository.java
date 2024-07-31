package dormitoryfamily.doomz.domain.board.article.repository;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
}
