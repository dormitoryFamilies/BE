package dormitoryfamily.doomz.global.jwt.refresh.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidTokenCategoryException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_TOKEN_CATEGORY;

    public InvalidTokenCategoryException() {
        super(ERROR_CODE);
    }
}
