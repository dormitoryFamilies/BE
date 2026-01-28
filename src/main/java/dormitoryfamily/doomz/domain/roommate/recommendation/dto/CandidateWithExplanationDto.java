package dormitoryfamily.doomz.domain.roommate.recommendation.dto;

public record CandidateWithExplanationDto(
        Long candidateId,
        String explanation
) {
    public static CandidateWithExplanationDto of(Long candidateId, String explanation) {
        return new CandidateWithExplanationDto(candidateId, explanation);
    }
}
