package dormitoryfamily.doomz.global.jwt.refresh.service;

import dormitoryfamily.doomz.global.jwt.JWTUtil;
import dormitoryfamily.doomz.global.jwt.refresh.entity.RefreshTokenEntity;
import dormitoryfamily.doomz.global.jwt.refresh.exception.InvalidTokenCategoryException;
import dormitoryfamily.doomz.global.jwt.refresh.exception.NotSavedRefreshTokenException;
import dormitoryfamily.doomz.global.jwt.refresh.exception.RefreshTokenExpiredException;
import dormitoryfamily.doomz.global.jwt.refresh.exception.RefreshTokenNotExistsException;
import dormitoryfamily.doomz.global.jwt.refresh.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String headerAuth = request.getHeader(HEADER_STRING_REFRESH);

        // RefreshToken 존재 유무 확인
        if (headerAuth == null) {
            throw new RefreshTokenNotExistsException();
        }

        String refresh = headerAuth.replace("Bearer ", "");

        // 만료 여부 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        }

        // 토큰 카테고리의 Refresh 여부 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals(CATEGORY_REFRESH)) {
            throw new InvalidTokenCategoryException();
        }

        String email = jwtUtil.getEmail(refresh);

        // 이미 DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByEmail(email);
        if (!isExist) {
            throw new NotSavedRefreshTokenException();
        }

        String newAccess = jwtUtil.createToken(CATEGORY_ACCESS, jwtUtil.getEmail(refresh), ACCESS_TOKEN_EXPIRATION_TIME);
        String newRefresh = jwtUtil.createToken(CATEGORY_REFRESH, jwtUtil.getEmail(refresh), REFRESH_TOKEN_EXPIRATION_TIME);

        // 기존 리프레시 토큰 레디스에서 삭제
        removeOldRefreshToken(email);

        // 새로운 리프레시 토큰 레디스에 저장
        saveNewRefreshToken(email, newRefresh);

        response.setHeader(HEADER_STRING_ACCESS, TOKEN_PREFIX + newAccess);
        response.setHeader(HEADER_STRING_REFRESH, TOKEN_PREFIX + newRefresh);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void removeOldRefreshToken(String email) {
        RefreshTokenEntity savedRefreshToken = refreshTokenRepository.findByEmail(email).get();
        refreshTokenRepository.delete(savedRefreshToken);
    }

    private void saveNewRefreshToken(String email, String refresh) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity(email, refresh);
        refreshTokenRepository.save(refreshToken);
    }
}
