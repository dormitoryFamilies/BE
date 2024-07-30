package dormitoryfamily.doomz.domain.roomateWish.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class CannotWishYourselfException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CANNOT_WISH_YOURSELF;

    public CannotWishYourselfException() {
        super(ERROR_CODE);
    }
}
