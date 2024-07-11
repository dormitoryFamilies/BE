package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record FollowerMemberResponseDto(
        Long memberId,
        String nickname,
        String profileUrl,
        boolean isFollowing
) implements MemberBasePagingResponseDto, MemberBaseResponseDto {

    public static FollowerMemberResponseDto fromEntity(Member member, boolean isFollowing) {
        return new FollowerMemberResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                isFollowing
        );
    }
}
