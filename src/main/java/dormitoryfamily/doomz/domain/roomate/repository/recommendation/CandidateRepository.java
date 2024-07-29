package dormitoryfamily.doomz.domain.roomate.repository.recommendation;

import dormitoryfamily.doomz.domain.roomate.entity.Candidate;
import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    
    List<Candidate> findAllByRecommendationIdOrderByCandidateScoreDesc(Long recommendationId);

    @Modifying
    @Query("DELETE FROM Candidate c WHERE c.recommendation = :recommendation")
    void deleteAllByRecommendation(@Param("recommendation") Recommendation recommendation);
}
