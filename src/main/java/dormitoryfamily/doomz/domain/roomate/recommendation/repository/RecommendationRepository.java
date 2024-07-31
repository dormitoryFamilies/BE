package dormitoryfamily.doomz.domain.roomate.recommendation.repository;

import dormitoryfamily.doomz.domain.roomate.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Optional<Recommendation> findByMemberId(Long memberId);
}
