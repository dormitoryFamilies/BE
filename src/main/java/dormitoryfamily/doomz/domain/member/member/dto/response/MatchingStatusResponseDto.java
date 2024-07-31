package dormitoryfamily.doomz.domain.member.member.dto.response;

import dormitoryfamily.doomz.domain.member.member.entity.Member;

public record MatchingStatusResponseDto(
        boolean isRoommateMatched
) {
    public static MatchingStatusResponseDto from(Member member){
        return new MatchingStatusResponseDto(member.isRoommateMatched());
    }
}
