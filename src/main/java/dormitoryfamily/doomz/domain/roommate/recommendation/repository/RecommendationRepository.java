package dormitoryfamily.doomz.domain.roommate.recommendation.repository;

import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @EntityGraph(attributePaths = "candidates")
    Optional<Recommendation> findByMemberId(Long memberId);
}
