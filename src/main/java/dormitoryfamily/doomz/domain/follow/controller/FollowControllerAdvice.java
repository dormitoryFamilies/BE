package dormitoryfamily.doomz.domain.follow.controller;

import dormitoryfamily.doomz.domain.article.exception.InvalidDormitoryTypeException;
import dormitoryfamily.doomz.domain.follow.exception.AlreadyFollowingException;
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
}
