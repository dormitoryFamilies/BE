package dormitoryfamily.doomz.domain.member.member.dto.response;

import dormitoryfamily.doomz.domain.member.member.entity.Member;

public record FollowerMemberResponseDto(
        Long memberId,
        String nickname,
        String profileUrl,
        boolean isFollowing
) implements MemberBaseResponseDto {

    public static FollowerMemberResponseDto fromEntity(Member member, boolean isFollowing) {
        return new FollowerMemberResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                isFollowing
        );
    }
}
