package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidSleepingSensitivityTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SLEEPING_SENSITIVITY_NOT_EXISTS;

    public InvalidSleepingSensitivityTypeException() {
        super(ERROR_CODE);
    }
}
