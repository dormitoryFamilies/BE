package dormitoryfamily.doomz.domain.member.member.dto.response;

public record MatchingStatusResponseDto(
        Long matchedId
) {
    public static MatchingStatusResponseDto from(Long matchedId){
        return new MatchingStatusResponseDto(matchedId);
    }
}
