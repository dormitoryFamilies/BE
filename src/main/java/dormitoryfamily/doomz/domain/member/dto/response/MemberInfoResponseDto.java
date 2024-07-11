package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record MemberInfoResponseDto(
        Long memberId,
        String nickname,
        String profileUrl
) implements MemberBasePagingResponseDto, MemberBaseResponseDto {

    public static MemberInfoResponseDto fromEntity(Member member) {
        return new MemberInfoResponseDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl()
        );
    }
}

