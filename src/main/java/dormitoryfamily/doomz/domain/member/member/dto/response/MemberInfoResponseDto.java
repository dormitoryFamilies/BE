package dormitoryfamily.doomz.domain.member.member.dto.response;

import dormitoryfamily.doomz.domain.member.member.entity.Member;

public record MemberInfoResponseDto(
        Long memberId,
        String nickname,
        String profileUrl
) implements MemberBaseResponseDto {

    public static MemberInfoResponseDto fromEntity(Member member) {
        return new MemberInfoResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl()
        );
    }
}

