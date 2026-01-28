package dormitoryfamily.doomz.domain.roommate.recommendation.dto;

import java.util.List;

public record RecommendationWithExplanationResponseDto(
        List<CandidateWithExplanationDto> candidates
) {
    public static RecommendationWithExplanationResponseDto of(List<CandidateWithExplanationDto> candidates) {
        return new RecommendationWithExplanationResponseDto(candidates);
    }
}
