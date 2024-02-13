package dormitoryfamily.doomz.domain.article.repository;

import dormitoryfamily.doomz.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
