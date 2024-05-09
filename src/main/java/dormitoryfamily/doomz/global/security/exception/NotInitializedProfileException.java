package dormitoryfamily.doomz.global.security.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NotInitializedProfileException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_INITIALIZE_PROFILE;

    public NotInitializedProfileException() {
        super(ERROR_CODE);
    }
}
