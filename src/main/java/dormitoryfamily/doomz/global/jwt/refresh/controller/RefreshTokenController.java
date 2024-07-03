package dormitoryfamily.doomz.global.jwt.refresh.controller;

import dormitoryfamily.doomz.global.jwt.refresh.service.RefreshTokenService;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto<Void>> reissue(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.reissue(request, response);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        String email = refreshTokenService.validateRefreshToken(request);
        refreshTokenService.removeOldRefreshToken(email);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ResponseDto.ok());
    }
}
