package dormitoryfamily.doomz.domain.roomate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidSleepingTimeTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SLEEPING_TIME_NOT_EXISTS;

    public InvalidSleepingTimeTypeException() {
        super(ERROR_CODE);
    }
}
