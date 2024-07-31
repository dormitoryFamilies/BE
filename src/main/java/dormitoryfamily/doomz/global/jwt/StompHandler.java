package dormitoryfamily.doomz.global.jwt;

import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.security.exception.AccessTokenNotExistsException;
import dormitoryfamily.doomz.global.security.exception.MemberDataNotExistsException;
import dormitoryfamily.doomz.global.security.exception.NotAccessTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.*;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            String headerAuth = accessor.getFirstNativeHeader(HEADER_STRING_ACCESS);

            if (headerAuth == null || !headerAuth.startsWith(TOKEN_PREFIX)) {
                throw new AccessTokenNotExistsException();
            }

            String jwt = headerAuth.replace(TOKEN_PREFIX, "");
            jwtUtil.isExpired(jwt);

            String category = jwtUtil.getCategory(jwt);
            if (!category.equals(CATEGORY_ACCESS)) {
                throw new NotAccessTokenException();
            }

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authentication = getUserAuthentication(jwt);
            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return message;
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
