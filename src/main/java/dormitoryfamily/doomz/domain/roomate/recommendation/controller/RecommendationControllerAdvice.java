package dormitoryfamily.doomz.domain.roomate.recommendation.controller;

import dormitoryfamily.doomz.domain.roomate.recommendation.exception.TooManyRequestException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RecommendationControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleTooManyRequestException(TooManyRequestException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();
        long remainingMinutes = e.getRemainingMinutes();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage() +
                        " 남은 시간 [" + remainingMinutes + "분]"));
    }
}
