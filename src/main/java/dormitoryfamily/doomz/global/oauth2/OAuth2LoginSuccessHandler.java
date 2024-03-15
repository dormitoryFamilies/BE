package dormitoryfamily.doomz.global.oauth2;

import dormitoryfamily.doomz.global.jwt.JWTUtil;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("====== OAuth2LoginSuccessHandler ======");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String token = jwtUtil.createToken(principalDetails.getMember());
        System.out.println("token = " + token);

        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:8080/");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
