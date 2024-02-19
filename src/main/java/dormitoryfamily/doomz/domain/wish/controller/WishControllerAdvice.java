package dormitoryfamily.doomz.domain.wish.controller;

import dormitoryfamily.doomz.domain.replyComment.exception.ReplyCommentNotExistsException;
import dormitoryfamily.doomz.domain.wish.exception.AlreadyWishedArticleException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class WishControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleAlreadyWishedArticleException(AlreadyWishedArticleException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
