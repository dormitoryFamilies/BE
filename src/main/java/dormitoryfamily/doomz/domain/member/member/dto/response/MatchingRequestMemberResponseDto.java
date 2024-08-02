package dormitoryfamily.doomz.domain.member.member.dto.response;

import dormitoryfamily.doomz.domain.member.member.entity.Member;

public record MatchingRequestMemberResponseDto(
        Long memberId,
        String nickname,
        String profileUrl,
        boolean isMatchable
) implements MemberBaseResponseDto {

    public static MatchingRequestMemberResponseDto fromEntity(Member member, boolean isMatchable) {
        return new MatchingRequestMemberResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                isMatchable
        );
    }
}
