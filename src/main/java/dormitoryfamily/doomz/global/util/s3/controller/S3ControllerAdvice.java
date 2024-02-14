package dormitoryfamily.doomz.global.util.s3.controller;

import dormitoryfamily.doomz.global.util.ResponseDto;
import dormitoryfamily.doomz.global.util.s3.exception.FileEmptyException;
import dormitoryfamily.doomz.global.util.s3.exception.FileNotExistsException;
import dormitoryfamily.doomz.global.util.s3.exception.FileUploadFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class S3ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> fileUploadExceptionHandler(FileUploadFailException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> fileEmptyExceptionHandler(FileEmptyException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> fileNotExistsExceptionHandler(FileNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
