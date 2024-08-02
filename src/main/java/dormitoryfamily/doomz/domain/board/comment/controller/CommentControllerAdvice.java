package dormitoryfamily.doomz.domain.board.comment.controller;

import dormitoryfamily.doomz.domain.board.comment.exception.CommentIsDeletedException;
import dormitoryfamily.doomz.domain.board.comment.exception.CommentNotExistsException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class CommentControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleCommentNotExistsException(CommentNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleCommentIsDeleted(CommentIsDeletedException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
