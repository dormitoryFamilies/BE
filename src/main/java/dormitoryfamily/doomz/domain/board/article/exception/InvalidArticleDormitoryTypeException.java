package dormitoryfamily.doomz.domain.board.article.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidArticleDormitoryTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_DORMITORY_TYPE_NOT_EXISTS;

    public InvalidArticleDormitoryTypeException() {
        super(ERROR_CODE);
    }
}
