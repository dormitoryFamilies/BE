package dormitoryfamily.doomz.domain.articleWish.repository;

import dormitoryfamily.doomz.domain.articleWish.entity.ArticleWish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleWishRepository extends JpaRepository<ArticleWish, Long> {

    List<ArticleWish> findAllByArticleIdOrderByCreatedAtDesc(Long articleId);

    boolean existsByMemberIdAndArticleId(Long memberId, Long articleId);

    Optional<ArticleWish> findByMemberIdAndArticleId(Long memberId, Long articleId);

    List<ArticleWish> findAllByMemberId(Long memberId);
}
