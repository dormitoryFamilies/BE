package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record MemberDetailsResponseDto (
        Long memberId,
        String nickname,
        String profileUrl,
        String dormitoryType,
        boolean isFollowing
) implements MemberBaseResponseDto{

    public static MemberDetailsResponseDto  fromEntity(Member member, boolean isFollowing) {
        return new MemberDetailsResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                member.getDormitoryType().getDescription(),
                isFollowing
        );
    }
}
