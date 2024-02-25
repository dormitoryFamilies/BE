package dormitoryfamily.doomz.domain.member.controller;

import dormitoryfamily.doomz.domain.member.exception.InvalidCollegeTypeException;
import dormitoryfamily.doomz.domain.member.exception.InvalidGenderTypeException;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleInvalidMemberAccessException(InvalidMemberAccessException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleInvalidGenderTypeException(InvalidGenderTypeException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleInvalidCollegeTypeException(InvalidCollegeTypeException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
