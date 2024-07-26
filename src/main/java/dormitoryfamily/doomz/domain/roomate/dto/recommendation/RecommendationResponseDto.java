package dormitoryfamily.doomz.domain.roomate.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormitoryfamily.doomz.domain.roomate.entity.Candidate;
import dormitoryfamily.doomz.domain.roomate.entity.Recommendation;

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
                    recommendation.getCreatedAt(),
                    candidates.stream()
                            .map(candidate -> candidate.getCandidateMember().getId())
                            .collect(Collectors.toList())
            );
    }
}
