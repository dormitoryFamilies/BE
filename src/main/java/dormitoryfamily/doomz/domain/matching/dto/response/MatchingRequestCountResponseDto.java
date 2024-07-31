package dormitoryfamily.doomz.domain.matching.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record MatchingRequestCountResponseDto(
        String nickname,
        long requestReceivedCount
) {
    public static MatchingRequestCountResponseDto from(Member member, long count){
        return new MatchingRequestCountResponseDto(member.getNickname(),count);
    }
}
