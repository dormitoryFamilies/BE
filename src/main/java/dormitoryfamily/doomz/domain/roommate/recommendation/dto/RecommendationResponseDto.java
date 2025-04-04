package dormitoryfamily.doomz.domain.roommate.recommendation.dto;



import java.util.List;


public record RecommendationResponseDto(
        Long recommendationId,
        List<Long> candidateIds
) {
    public static RecommendationResponseDto of(Long recommendationId, List<Long> candidateIds) {
        return new RecommendationResponseDto(recommendationId, candidateIds);
    }

    public static RecommendationResponseDto empty() {
        return new RecommendationResponseDto(null, List.of());
    }
}
