package dormitoryfamily.doomz.global.security.exception.handler;

import dormitoryfamily.doomz.global.security.exception.NotAccessTokenException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof NotAccessTokenException) {
                NotAccessTokenException notAccessTokenException = (NotAccessTokenException) e;
                handleAuthenticationException(e);
            }
        }
    }

    private ResponseEntity<ResponseDto<Void>> handleAuthenticationException(Exception e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
