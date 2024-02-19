package dormitoryfamily.doomz.domain.article.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class StatusAlreadySetException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.STATUS_ALREADY_SET;

    public StatusAlreadySetException(String message) {
        super(ERROR_CODE, message);
    }
}
