package dormitoryfamily.doomz.domain.roomate.repository.recommendation;

import dormitoryfamily.doomz.domain.roomate.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    
    List<Candidate> findAllByRecommendationIdOrderByCandidateScoreDesc(Long recommendationId);
}
