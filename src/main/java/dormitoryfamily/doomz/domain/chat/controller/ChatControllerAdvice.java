package dormitoryfamily.doomz.domain.chat.controller;

import dormitoryfamily.doomz.domain.chat.exception.ChatNotExistsException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ChatControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleChatNotExistsException(ChatNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
