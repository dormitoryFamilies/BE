package dormitoryfamily.doomz.domain.chatting.chat.controller;

import dormitoryfamily.doomz.domain.chatting.chat.exception.ChatNotExistsException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleChatNotExistsException(ChatNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
