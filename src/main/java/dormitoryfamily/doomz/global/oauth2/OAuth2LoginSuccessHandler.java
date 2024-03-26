package dormitoryfamily.doomz.global.oauth2;

import dormitoryfamily.doomz.global.jwt.JWTUtil;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("====== OAuth2LoginSuccessHandler ======");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getEmail();

        // 토큰 생성
        String accessToken = jwtUtil.createToken(CATEGORY_ACCESS, email, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtUtil.createToken(CATEGORY_REFRESH, email, REFRESH_TOKEN_EXPIRATION_TIME);
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);

        // 응답 설정
        response.addHeader(HEADER_STRING_ACCESS, TOKEN_PREFIX + accessToken);
        response.addHeader(HEADER_STRING_REFRESH, TOKEN_PREFIX + refreshToken);
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("http://43.202.254.127:8080/");
    }


}
