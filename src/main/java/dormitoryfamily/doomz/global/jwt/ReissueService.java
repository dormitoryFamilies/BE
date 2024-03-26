package dormitoryfamily.doomz.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.*;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String headerAuth = request.getHeader(HEADER_STRING_REFRESH);
        String refresh = headerAuth.replace("Bearer ", "");
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals(CATEGORY_REFRESH)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String newAccess = jwtUtil.createToken(CATEGORY_ACCESS, jwtUtil.getEmail(refresh), ACCESS_TOKEN_EXPIRATION_TIME);

        response.setHeader(HEADER_STRING_ACCESS, TOKEN_PREFIX + newAccess);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
