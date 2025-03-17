package dormitoryfamily.doomz.domain.chatting.chatroom.controller;

import dormitoryfamily.doomz.domain.chatting.chatroom.exception.*;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatRoomControllerAdvice {

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public ResponseEntity<ResponseDto<Void>> handleInvalidChatMessageException(MemberNotInChatRoomException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
