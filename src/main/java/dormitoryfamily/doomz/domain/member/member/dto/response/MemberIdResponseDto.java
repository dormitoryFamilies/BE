package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record MemberIdResponseDto(
        long memberId
) {
    public static MemberIdResponseDto from(Member member){
        return new MemberIdResponseDto(member.getId());
    }
}
