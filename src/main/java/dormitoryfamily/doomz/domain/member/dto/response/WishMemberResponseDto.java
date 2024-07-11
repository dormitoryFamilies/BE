package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record WishMemberResponseDto(
        Long memberId,
        String nickname,
        String profileUrl,
        String dormitory
) {

    public static WishMemberResponseDto fromEntity(Member member) {
        return new WishMemberResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl(),
                member.getDormitoryType().getDescription()
        );
    }
}