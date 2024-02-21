package dormitoryfamily.doomz.domain.wish.dto;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record WishMemberResponseDto(
        Long memberId,
        String nickName,
        String dormitoryType,
        String profileUrl
) {
    public static WishMemberResponseDto fromMember(Member wishMember) {
        return new WishMemberResponseDto(
                wishMember.getId(),
                wishMember.getNickname(),
                wishMember.getDormitoryType().getName(),
                wishMember.getProfileUrl()
        );
    }
}

