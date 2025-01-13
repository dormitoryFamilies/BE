package dormitoryfamily.doomz.global.oauth2.controller;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.HEADER_STRING_ACCESS;
import static dormitoryfamily.doomz.global.jwt.JWTProperties.HEADER_STRING_REFRESH;

import dormitoryfamily.doomz.global.oauth2.dto.AccessTokenRequestDto;
import dormitoryfamily.doomz.global.oauth2.dto.JwtResponseDto;
import dormitoryfamily.doomz.global.oauth2.service.UserKakaoService;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserKakaoController {

    private final UserKakaoService UserKakaoService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(
            @RequestBody AccessTokenRequestDto request
    ) {
        JwtResponseDto responseDto = UserKakaoService.getJWTAccessToken(request.accessToken());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HEADER_STRING_ACCESS, responseDto.accessToken())
                .header(HEADER_STRING_REFRESH, responseDto.refreshToken())
                .body(ResponseDto.ok());
    }
}
