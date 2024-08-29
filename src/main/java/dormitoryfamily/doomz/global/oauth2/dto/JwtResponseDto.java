package dormitoryfamily.doomz.global.oauth2.dto;

public record JwtResponseDto (
        String accessToken,
        String refreshToken
){
}
