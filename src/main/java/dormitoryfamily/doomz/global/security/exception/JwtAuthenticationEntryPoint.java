package dormitoryfamily.doomz.global.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;
    private final ObjectMapper objectMapper;


    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, ObjectMapper objectMapper) {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        resolver.resolveException(request, response, null, (Exception) request.getAttribute("exception"));
//        log.error("Not Authenticated Request", authException);
//        log.error("Request Uri : {}", request.getRequestURI());
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.errorWithMessage(HttpStatus.UNAUTHORIZED, "zzzzzzzz"))));
    }
}
