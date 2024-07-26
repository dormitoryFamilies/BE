package dormitoryfamily.doomz.domain.roomate.repository.recommendation;

import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
