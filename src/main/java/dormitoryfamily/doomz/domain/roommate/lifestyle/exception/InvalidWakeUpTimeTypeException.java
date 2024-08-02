package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidWakeUpTimeTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.WAKE_UP_TIME_NOT_EXISTS;

    public InvalidWakeUpTimeTypeException() {
        super(ERROR_CODE);
    }
}
