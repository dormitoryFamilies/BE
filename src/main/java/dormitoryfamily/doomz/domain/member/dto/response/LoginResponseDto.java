package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;

public record LoginResponseDto(
        String email,
        String name,
        String token
) {

    public static LoginResponseDto fromEntity(Member member, String token) {
        return new LoginResponseDto(
                member.getEmail(),
                member.getNickname(),
                token
        );
    }
}