package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidInsectToleranceTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INSECT_TOLERANCE_NOT_EXISTS;

    public InvalidInsectToleranceTypeException() {
        super(ERROR_CODE);
    }
}
