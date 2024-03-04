package dormitoryfamily.doomz.global.jwt;

import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //헤더에서 jwt 추출
        List<String> headerValues = Collections.list(request.getHeaders("Authorization"));
        String jwt = headerValues.stream()
                .findFirst()
                .map(header -> header.replace("Bearer ", ""))
                .orElse(null);

        //토큰을 사용하여 인증 시도
        Authentication authentication = getUsernamePasswordAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication); //세션에 등록

        chain.doFilter(request, response);
    }

    /**
     * JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken 반환
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token) {
        if (token == null) {
            return null;
        }
        String email = jwtUtil.getEmail(token);
        if (email != null) {
            return memberRepository.findByEmail(email)
                    .map(PrincipalDetails::new)
                    .map(principalDetails -> new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null,
                            principalDetails.getAuthorities()
                    )).orElseThrow(IllegalAccessError::new);
        }
        return null; //유저가 없으면 null
    }
}
