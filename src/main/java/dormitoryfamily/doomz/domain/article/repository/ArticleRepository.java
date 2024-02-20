package dormitoryfamily.doomz.domain.article.repository;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE a.dormitoryType = :dormitoryType")
    Slice<Article> findByArticleDormitoryType(@Param("dormitoryType") ArticleDormitoryType dormitoryType);
}
