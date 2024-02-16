package dormitoryfamily.doomz.domain.article.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class ArticleNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_NOT_EXISTS;

    public ArticleNotExistsException() {
        super(ERROR_CODE);
    }
}
