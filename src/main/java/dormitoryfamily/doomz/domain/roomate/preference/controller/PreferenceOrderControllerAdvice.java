package dormitoryfamily.doomz.domain.roomate.preference.controller;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import dormitoryfamily.doomz.domain.roomate.preference.exception.DuplicatePreferenceOrderException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static dormitoryfamily.doomz.global.exception.ErrorCode.DUPLICATE_PREFERENCE_ORDER_PARAMETER;
import static dormitoryfamily.doomz.global.exception.ErrorCode.WRONG_PROPERTY;

@RestControllerAdvice
public class PreferenceOrderControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String wrongProperty = e.getPropertyName();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, "[" + wrongProperty + "] " + WRONG_PROPERTY.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleDuplicatePreferenceOrderException(DuplicatePreferenceOrderException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String duplicatePreference = e.getDuplicatePreference();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, "[" + duplicatePreference + "] " + DUPLICATE_PREFERENCE_ORDER_PARAMETER.getMessage()));
    }
}
