package dormitoryfamily.doomz.global.jwt.refresh.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class RefreshTokenNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.REFRESH_TOKEN_NOT_EXISTS;

    public RefreshTokenNotExistsException() {
        super(ERROR_CODE);
    }
}
