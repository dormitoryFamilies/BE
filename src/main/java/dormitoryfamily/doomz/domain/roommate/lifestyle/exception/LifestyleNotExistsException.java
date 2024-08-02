package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class LifestyleNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.LIFESTYLE_NOT_EXISTS;

    public LifestyleNotExistsException() {
        super(ERROR_CODE);
    }
}
