package dormitoryfamily.doomz.domain.article.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidStatusTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.STATUS_TYPE_NOT_EXISTS;

    public InvalidStatusTypeException() {
        super(ERROR_CODE);
    }
}
