package dormitoryfamily.doomz.global.jwt;

import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.security.exception.MemberDataNotExistsException;
import dormitoryfamily.doomz.global.security.exception.NotAccessTokenException;
import dormitoryfamily.doomz.global.security.exception.NotInitializedProfileException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

import static dormitoryfamily.doomz.domain.member.entity.type.RoleType.ROLE_VISITOR;
import static dormitoryfamily.doomz.global.jwt.JWTProperties.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("====== JWTAuthorizationFilter ======");

//        String headerAuth = request.getHeader(HEADER_STRING_ACCESS);
//        String jwt = "";
//
//        if (headerAuth == null || !headerAuth.startsWith("Bearer")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        jwt = headerAuth.replace(TOKEN_PREFIX, "");
//
//        // 필터 만료 여부 확인
//        jwtUtil.isExpired(jwt);
//
//        String category = jwtUtil.getCategory(jwt);
//        if (!category.equals(CATEGORY_ACCESS)) {
//            chain.doFilter(request, response);
//            return;
//        }

//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authentication = getUserAuthentication(jwt);
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        chain.doFilter(request, response);

        String headerAuth = request.getHeader(HEADER_STRING_ACCESS);
        String jwt = "";

        try {
            // 토큰 값 자체 유무 확인
            if (headerAuth == null || !headerAuth.startsWith("Bearer")) {
                throw new NotAccessTokenException();
            }
            jwt = headerAuth.replace(TOKEN_PREFIX, "");

            // 필터 만료 여부 확인
            jwtUtil.isExpired(jwt);

            // 엑세스 토큰 여부 확인
            String category = jwtUtil.getCategory(jwt);
            if (!category.equals(CATEGORY_ACCESS)) {
                throw new NotAccessTokenException();
            }

            String role = jwtUtil.getRole(jwt);
            if (role.equals(ROLE_VISITOR.toString())) {
                throw new NotInitializedProfileException();
            }

            //스프링 시큐리티 인증 토큰 생성
            Authentication authentication = getUserAuthentication(jwt);
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            request.setAttribute("exception", e);
        } finally {
            chain.doFilter(request, response);
        }
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
                    )).orElseThrow(MemberDataNotExistsException::new);
        }
        return null; //유저가 없으면 null
    }
}
