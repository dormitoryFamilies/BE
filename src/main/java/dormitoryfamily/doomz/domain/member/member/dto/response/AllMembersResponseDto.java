package dormitoryfamily.doomz.domain.member.member.dto.response;

import dormitoryfamily.doomz.domain.member.member.entity.Member;

public record AllMembersResponseDto (
        Long memberId,
        String nickname,
        String profileUrl,
        boolean isFollowing,
        boolean isRoommateWished
) implements MemberBaseResponseDto {

    public static AllMembersResponseDto fromEntity(Member member, boolean isFollowing, boolean isRoommateWished) {
        return new AllMembersResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                isFollowing,
                isRoommateWished
        );
    }
}