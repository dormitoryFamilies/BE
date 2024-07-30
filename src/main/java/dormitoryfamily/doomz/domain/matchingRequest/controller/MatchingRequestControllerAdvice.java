package dormitoryfamily.doomz.domain.matchingRequest.controller;

import dormitoryfamily.doomz.domain.matchingRequest.exception.CannotMatchingYourselfException;
import dormitoryfamily.doomz.domain.matchingRequest.exception.MatchingRequestAlreadyExitsException;
import dormitoryfamily.doomz.domain.matchingRequest.exception.MatchingRequestNotExistException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MatchingRequestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleMatchingRequestNotExistException(MatchingRequestNotExistException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleCannotMatchingYourselfException(CannotMatchingYourselfException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleMatchingRequestAlreadyExitsException(MatchingRequestAlreadyExitsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
