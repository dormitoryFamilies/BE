package dormitoryfamily.doomz.domain.article.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidDormitoryTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_DORMITORY_TYPE_NOT_EXISTS;

    public InvalidDormitoryTypeException() {
        super(ERROR_CODE);
    }
}
