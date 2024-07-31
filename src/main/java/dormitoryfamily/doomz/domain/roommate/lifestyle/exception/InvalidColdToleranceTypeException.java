package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidColdToleranceTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.COLD_TOLERANCE_NOT_EXISTS;

    public InvalidColdToleranceTypeException() {
        super(ERROR_CODE);
    }
}
