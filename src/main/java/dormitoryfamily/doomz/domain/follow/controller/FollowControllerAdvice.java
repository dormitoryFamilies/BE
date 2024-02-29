package dormitoryfamily.doomz.domain.follow.controller;

import dormitoryfamily.doomz.domain.follow.exception.AlreadyFollowingException;
import dormitoryfamily.doomz.domain.follow.exception.CannotFollowYourselfException;
import dormitoryfamily.doomz.domain.follow.exception.NotFollowingMemberException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class FollowControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleAlreadyFollowingException(AlreadyFollowingException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleCannotFollowYourselfException(CannotFollowYourselfException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleNotFollowingMemberException(NotFollowingMemberException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
