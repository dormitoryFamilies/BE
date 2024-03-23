package dormitoryfamily.doomz.global.jwt;

import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("====== JWTAuthorizationFilter ======");
        String jwt = null;

        String headerAuth = request.getHeader(JWTProperties.HEADER_STRING);

        if (headerAuth == null || !headerAuth.startsWith("Bearer") || jwtUtil.isExpired(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        jwt = headerAuth.replace(JWTProperties.TOKEN_PREFIX, "");
        log.info("jwt ={}", jwt);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authentication = getUserAuthentication(jwt);
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private Authentication getUserAuthentication(String token) {
        if (token == null) {
            return null;
        }

        String email = jwtUtil.getEmail(token);
        if (email != null) {
            return memberRepository.findByEmail(email)
                    .map(PrincipalDetails::new)
                    .map(principalDetails -> new UsernamePasswordAuthenticationToken(
                            principalDetails, null, principalDetails.getAuthorities()
                    )).orElseThrow(IllegalAccessError::new);
        }
        return null; //유저가 없으면 null
    }
}
