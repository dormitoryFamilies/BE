package dormitoryfamily.doomz.domain.wish.repository;

import dormitoryfamily.doomz.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsByMemberIdAndArticleId(Long memberId, Long articleId);

    Optional<Wish> findByMemberIdAndArticleId(Long memberId, Long articleId);
}
