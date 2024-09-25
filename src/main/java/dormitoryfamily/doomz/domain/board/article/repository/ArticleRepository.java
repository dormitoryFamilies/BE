package dormitoryfamily.doomz.domain.board.article.repository;

import dormitoryfamily.doomz.domain.board.article.entity.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = {"member", "articleImages"})
    Optional<Article> findById(Long id);

    @Query("SELECT a FROM Article a WHERE a.id = :id")
    Optional<Article> findByIdWithoutFetch(Long id);
}
