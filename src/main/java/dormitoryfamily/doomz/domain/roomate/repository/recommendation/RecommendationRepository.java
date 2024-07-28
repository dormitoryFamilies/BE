package dormitoryfamily.doomz.domain.roomate.repository.recommendation;

import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Optional<Recommendation> findByMemberId(Long memberId);
}
