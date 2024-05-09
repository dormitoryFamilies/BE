package dormitoryfamily.doomz.global.security.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AccessTokenNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ACCESS_TOKEN_NOT_EXISTS;

    public AccessTokenNotExistsException() {
        super(ERROR_CODE);
    }
}
