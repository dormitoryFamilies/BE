package dormitoryfamily.doomz.global.security.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Autowired
    public JWTAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws ServletException, IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");

        String message;
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/verify/") && !request.isUserInRole("ROLE_ADMIN")) {
            message = "관리자 권한이 필요합니다.";
        } else if (request.isUserInRole("ROLE_VISITOR")) {
            message = "프로필 초기 설정이 필요합니다.";
        } else if (request.isUserInRole("ROLE_MEMBER")) {
            message = "학생증 인증이 완료되지 않았습니다.";
        } else if (request.isUserInRole("ROLE_REJECTED_MEMBER")) {
            message = "학생증 승인이 거부되었습니다.";
        } else {
            message = "접근 권한이 없는 사용자입니다.";
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("code", Integer.parseInt("403"));
        responseData.put("errorMessage", message);

        String responseBody = objectMapper.writeValueAsString(responseData);
        response.getWriter().write(responseBody);
    }
}
