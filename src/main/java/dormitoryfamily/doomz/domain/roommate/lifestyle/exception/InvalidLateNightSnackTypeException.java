package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidLateNightSnackTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.LATE_NIGHT_SNACK_NOT_EXISTS;

    public InvalidLateNightSnackTypeException() {
        super(ERROR_CODE);
    }
}
