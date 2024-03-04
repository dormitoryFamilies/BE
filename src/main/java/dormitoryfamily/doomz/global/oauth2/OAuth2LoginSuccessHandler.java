package dormitoryfamily.doomz.global.oauth2;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.jwt.JWTUtil;
import dormitoryfamily.doomz.global.security.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getDetails();

        String token = jwtUtil.createToken(principalDetails.getMember());
        String email = principalDetails.getEmail();
        String name = principalDetails.getName();

        //한국어 인코딩
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String redirectUrl = "http://localhost/auth/social?token=" + token + "&email=" + email + "&name=" + encodedName;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

//        response.addCookie(createCookie("Authorization", token));
//        response.sendRedirect("http://localhost:3000");
    }

//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(60 * 60 * 60);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//
//        return cookie;
//    }
}
