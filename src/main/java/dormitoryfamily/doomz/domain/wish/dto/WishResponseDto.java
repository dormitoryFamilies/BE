package dormitoryfamily.doomz.domain.wish.dto;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.entity.type.MemberDormitoryType;

public record WishResponseDto(
        Long memberId,
        String nickName,
        MemberDormitoryType dormitoryType,
        String profileUrl
) {
    public static WishResponseDto fromEntity(Member wishMember) {
        return new WishResponseDto(
                wishMember.getId(),
                wishMember.getNickname(),
                wishMember.getDormitoryType(),
                wishMember.getProfileUrl()
        );
    }
}

