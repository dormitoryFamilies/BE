package dormitoryfamily.doomz.domain.roommate.lifestyle.controller;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidLifestyleTypeException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static dormitoryfamily.doomz.global.exception.ErrorCode.LIFESTYLE_TYPE_NOT_EXISTS;

@RestControllerAdvice
public class LifestyleControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleInvalidLifestyleTypeException(InvalidLifestyleTypeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String wrongPreferenceType = e.getValue();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, "[" + wrongPreferenceType + "] " + LIFESTYLE_TYPE_NOT_EXISTS.getMessage()));
    }
}
