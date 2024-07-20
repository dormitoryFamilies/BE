package dormitoryfamily.doomz.domain.roomate.controller;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static dormitoryfamily.doomz.global.exception.ErrorCode.LIFE_STYLE_NOT_EXISTS;

@RestControllerAdvice
public class RoommateMatchingControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String wrongLifestyle = e.getPropertyName();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, wrongLifestyle + " " + LIFE_STYLE_NOT_EXISTS.getMessage()));
    }
}
