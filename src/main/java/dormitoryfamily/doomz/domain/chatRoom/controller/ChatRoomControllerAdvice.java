package dormitoryfamily.doomz.domain.chatRoom.controller;

import dormitoryfamily.doomz.domain.chatRoom.exception.*;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatRoomControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleCannotChatYourselfException(CannotChatYourselfException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleChatRoomNotExistsException(ChatRoomNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleAlreadyChatRoomLeftException(AlreadyChatRoomLeftException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleAlreadyInChatRoomException(AlreadyInChatRoomException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleChatRoomNotEmptyException(ChatRoomNotEmptyException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleMemberChatRoomNotExistsException(MemberChatRoomNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public ResponseEntity<ResponseDto<Void>> handleInvalidChatMessageException(MemberNotInChatRoomException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
