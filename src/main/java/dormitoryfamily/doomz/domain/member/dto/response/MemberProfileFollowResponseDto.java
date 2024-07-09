package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record MemberProfileFollowResponseDto(
        Long memberId,
        String nickname,
        String profileUrl,
        boolean isFollowing
) implements MemberProfileBaseResponseDto {

    public static MemberProfileFollowResponseDto fromEntity(Member member, boolean isFollowing) {
        return new MemberProfileFollowResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                isFollowing
        );
    }
}
