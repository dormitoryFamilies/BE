package dormitoryfamily.doomz.global.jwt.refresh.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NotSavedRefreshTokenException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_SAVED_REFRESH_TOKEN;

    public NotSavedRefreshTokenException() {
        super(ERROR_CODE);
    }
}
