package dormitoryfamily.doomz.global.oauth2.controller;

import dormitoryfamily.doomz.global.oauth2.dto.AccessTokenRequestDto;
import dormitoryfamily.doomz.global.oauth2.dto.JwtResponseDto;
import dormitoryfamily.doomz.global.oauth2.service.UserKakaoService;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserKakaoController {

    private final UserKakaoService UserKakaoService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(
            @RequestBody AccessTokenRequestDto request,
            HttpServletResponse httpServletResponse
    ) {
        JwtResponseDto responseDto = UserKakaoService.getJWTAccessToken(request.accessToken());
        httpServletResponse.setHeader("accessToken", responseDto.accessToken());
        httpServletResponse.setHeader("refreshToken", responseDto.refreshToken());

        return ResponseEntity.ok(ResponseDto.ok());
    }
}
