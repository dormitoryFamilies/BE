package dormitoryfamily.doomz.global.jwt;

import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
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

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("====== StompHandler ======");
        String jwt = null;
        String headerAuth = null;

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            headerAuth = accessor.getFirstNativeHeader("Authorization");
            if (headerAuth != null) {
                jwt = headerAuth.substring(7);
            }
        }

        System.out.println("headerAuth = " + headerAuth);
        System.out.println("jwt = " + jwt);

        if (jwt == null || jwtUtil.isExpired(jwt)) {
            throw new RuntimeException();
        }

        log.info("jwt ={}", jwt);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authentication = getUserAuthentication(jwt);
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
                    )).orElseThrow(IllegalAccessError::new);
        }
        return null; //유저가 없으면 null
    }
}
