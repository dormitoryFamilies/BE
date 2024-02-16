package dormitoryfamily.doomz.domain.article.controller;

import dormitoryfamily.doomz.domain.article.exception.ArticleDormitoryTypeNotExistsException;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.exception.BoardTypeNotExistsException;
import dormitoryfamily.doomz.global.util.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ArticleControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleArticleDormitoryTypeNotExistsException(ArticleDormitoryTypeNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleBoardTypeNotExistsException(BoardTypeNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleArticleNotExistsException(ArticleNotExistsException e) {
        HttpStatus status = e.getErrorCode().getHttpStatus();

        return ResponseEntity
                .status(status)
                .body(ResponseDto.errorWithMessage(status, e.getMessage()));
    }
}
