package dormitoryfamily.doomz.global.exception;

import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionRestAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleApplicationException(ApplicationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.error(e.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleBindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.errorWithMessage(
                        HttpStatus.BAD_REQUEST,
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                ));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleDbException(DataAccessException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "DB 에러!"));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleServerException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러!"));
    }

    //@Validate 검증 예외 처리
    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        Optional<FieldError> firstError = bindingResult.getFieldErrors().stream().findFirst();

        FieldError error = firstError.get();
        String errorMessage = error.getField() + ": " + error.getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.errorWithMessage(HttpStatus.BAD_REQUEST, errorMessage));
    }
}
