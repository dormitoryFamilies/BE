package dormitoryfamily.doomz.global.security.exception.handler;

import dormitoryfamily.doomz.global.security.exception.AccessTokenNotExistsException;
import dormitoryfamily.doomz.global.security.exception.MemberDataNotExistsException;
import dormitoryfamily.doomz.global.security.exception.NotAccessTokenException;
import dormitoryfamily.doomz.global.security.exception.NotInitializedProfileException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseDto<Void>> handleExpiredJwtException(ExpiredJwtException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, "엑세스 토큰이 만료되었습니다."));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ResponseDto<Void>> handleSignatureException(SignatureException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, "토큰이 유효하지 않습니다."));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ResponseDto<Void>> handleMalformedJwtException(MalformedJwtException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, "토큰이 올바른 형태가 아닙니다."));
    }

    @ExceptionHandler(AccessTokenNotExistsException.class)
    public ResponseEntity<ResponseDto<Void>> handleTokenNotExistsException(AccessTokenNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler(NotAccessTokenException.class)
    public ResponseEntity<ResponseDto<Void>> handleNotAccessTokenException(NotAccessTokenException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler(NotInitializedProfileException.class)
    public ResponseEntity<ResponseDto<Void>> handleNotInitializedProfileException(NotInitializedProfileException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler(MemberDataNotExistsException.class)
    public ResponseEntity<ResponseDto<Void>> handleMemberDataNotExistsException(MemberDataNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
