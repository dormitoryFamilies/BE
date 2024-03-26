package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record MemberProfileResponseDto(
        Long memberId,
        String nickname,
        String memberDormitory,
        String profileUrl
){
    public static MemberProfileResponseDto fromEntity(Member member){
        return new MemberProfileResponseDto(
                member.getId(),
                member.getNickname(),
                member.getDormitoryType().getName(),
                member.getProfileUrl()
        );
    }
}
