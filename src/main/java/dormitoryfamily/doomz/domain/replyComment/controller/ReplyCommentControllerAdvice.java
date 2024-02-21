package dormitoryfamily.doomz.domain.replyComment.controller;

import dormitoryfamily.doomz.domain.replyComment.exception.ReplyCommentNotExistsException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ReplyCommentControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleReplyCommentNotExistsException(ReplyCommentNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
