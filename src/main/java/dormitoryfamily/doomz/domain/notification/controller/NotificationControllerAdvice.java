package dormitoryfamily.doomz.domain.notification.controller;

import dormitoryfamily.doomz.domain.notification.exception.NotMyNotificationException;
import dormitoryfamily.doomz.domain.notification.exception.NotificationAlreadyReadException;
import dormitoryfamily.doomz.domain.notification.exception.NotificationNotExistsException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleNotificationAlreadyReadException(NotificationAlreadyReadException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();
        Long alreadyReadNotificationId = e.getAlreadyReadNotificationId();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage() + " [" + alreadyReadNotificationId + "]"));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleNotificationNotExistsException(NotificationNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();
        Long notExistsId = e.getNotExistsId();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage() + " [" + notExistsId + "]"));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleNotMyNotificationException(NotMyNotificationException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();
        Long wrongId = e.getWrongId();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage() + " [" + wrongId + "]"));
    }
}
