package dormitoryfamily.doomz.global.jwt.refresh.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class RefreshTokenExpiredException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.REFRESH_TOKEN_EXPIRED;

    public RefreshTokenExpiredException() {
        super(ERROR_CODE);
    }
}
