package dormitoryfamily.doomz.global.security.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NotAccessTokenException extends ApplicationException {
    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_ACCESS_TOKEN;

    public NotAccessTokenException() {
        super(ERROR_CODE);
    }
}
