package dormitoryfamily.doomz.domain.roommate.matching.controller;

import dormitoryfamily.doomz.domain.roommate.matching.exception.MatchingResultNotExistException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingResultControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleMatchingResultNotExistException(MatchingResultNotExistException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

}
