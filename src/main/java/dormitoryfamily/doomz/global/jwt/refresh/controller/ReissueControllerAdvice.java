package dormitoryfamily.doomz.global.jwt.refresh.controller;

import dormitoryfamily.doomz.global.jwt.refresh.exception.InvalidTokenCategoryException;
import dormitoryfamily.doomz.global.jwt.refresh.exception.NotSavedRefreshTokenException;
import dormitoryfamily.doomz.global.jwt.refresh.exception.RefreshTokenExpiredException;
import dormitoryfamily.doomz.global.jwt.refresh.exception.RefreshTokenNotExistsException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReissueControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleInvalidTokenCategoryException(InvalidTokenCategoryException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleNotSavedRefreshTokenException(NotSavedRefreshTokenException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleRefreshTokenExpiredException(RefreshTokenExpiredException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleRefreshTokenNotExistsException(RefreshTokenNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
