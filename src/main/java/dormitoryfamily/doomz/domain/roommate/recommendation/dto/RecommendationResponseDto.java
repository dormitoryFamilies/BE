package dormitoryfamily.doomz.domain.roommate.recommendation.dto;



import java.util.List;


public record RecommendationResponseDto(
        List<Long> candidateIds
) {
    public static RecommendationResponseDto of(List<Long> candidateIds) {
        return new RecommendationResponseDto(candidateIds);
    }
}
