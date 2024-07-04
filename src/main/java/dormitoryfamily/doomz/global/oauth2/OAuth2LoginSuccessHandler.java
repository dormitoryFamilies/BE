package dormitoryfamily.doomz.global.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.jwt.JWTUtil;
import dormitoryfamily.doomz.global.jwt.refresh.entity.RefreshTokenEntity;
import dormitoryfamily.doomz.global.jwt.refresh.repository.RefreshTokenRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        // 토큰 생성
        String accessToken = jwtUtil.createToken(CATEGORY_ACCESS, member, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtUtil.createToken(CATEGORY_REFRESH, member, REFRESH_TOKEN_EXPIRATION_TIME);

        // 리프레시 토큰 레디스에 저장
        saveNewRefreshToken(member.getEmail(), refreshToken);
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);

        // 응답 설정
        response.addHeader(HEADER_STRING_ACCESS, TOKEN_PREFIX + accessToken);
        response.addHeader(HEADER_STRING_REFRESH, TOKEN_PREFIX + refreshToken);
        response.setStatus(HttpStatus.OK.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("code", 200);
        responseData.put("message", "로그인에 성공했습니다.");

        String responseBody = objectMapper.writeValueAsString(responseData);
        response.getWriter().write(responseBody);
    }

    private void saveNewRefreshToken(String email, String refresh) {

        RefreshTokenEntity refreshToken = new RefreshTokenEntity(email, refresh);
        refreshTokenRepository.save(refreshToken);
    }
}
