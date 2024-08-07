package dormitoryfamily.doomz.domain.board.wish.controller;

import dormitoryfamily.doomz.domain.board.wish.exception.AlreadyWishedArticleException;
import dormitoryfamily.doomz.domain.board.wish.exception.CannotWishYourArticleException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ArticleWishControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleAlreadyWishedArticleException(AlreadyWishedArticleException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleCannotWishYourArticleException(CannotWishYourArticleException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
