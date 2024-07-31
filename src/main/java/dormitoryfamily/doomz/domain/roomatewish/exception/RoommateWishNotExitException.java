package dormitoryfamily.doomz.domain.roomatewish.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class RoommateWishNotExitException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ROOMMATE_WISH_NOT_EXIT;

    public RoommateWishNotExitException() {
        super(ERROR_CODE);
    }
}
