package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record FollowerMemberResponseDto(
        Long memberId,
        String nickname,
        String profileUrl,
        boolean isFollowing
) implements MemberProfileBaseResponseDto {

    public static FollowerMemberResponseDto fromEntity(Member member, boolean isFollowing) {
        return new FollowerMemberResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                isFollowing
        );
    }
}
