package dormitoryfamily.doomz.domain.wish.repository;

import dormitoryfamily.doomz.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findByArticleId(Long articleId);

    boolean existsByMemberIdAndArticleId(Long memberId, Long articleId);

    Optional<Wish> findByMemberIdAndArticleId(Long memberId, Long articleId);

    List<Wish> findWishesByMemberId(Long memberId);
}
