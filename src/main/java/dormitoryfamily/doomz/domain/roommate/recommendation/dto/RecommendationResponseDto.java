package dormitoryfamily.doomz.domain.roommate.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Candidate;
import dormitoryfamily.doomz.domain.roommate.recommendation.entity.Recommendation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record RecommendationResponseDto(

        Long recommendationId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime recommendedAt,
        List<Long> candidateIds

) {
    public static RecommendationResponseDto fromEntity(Recommendation recommendation, List<Candidate> candidates) {
            return new RecommendationResponseDto(
                    recommendation.getId(),
                    recommendation.getRecommendedAt(),
                    candidates.stream()
                            .map(candidate -> candidate.getCandidateMember().getId())
                            .collect(Collectors.toList())
            );
    }
}
