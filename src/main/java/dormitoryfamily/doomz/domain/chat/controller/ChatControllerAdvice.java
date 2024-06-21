package dormitoryfamily.doomz.domain.chat.controller;

import dormitoryfamily.doomz.domain.chat.exception.ChatNotExistsException;
import dormitoryfamily.doomz.domain.chat.exception.InvalidChatMessageException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatControllerAdvice {

    @MessageExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleInvalidChatMessageException(InvalidChatMessageException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleChatNotExistsException(ChatNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
