package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidVisitHomeFrequencyTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.VISIT_HOME_FREQUENCY_NOT_EXISTS;

    public InvalidVisitHomeFrequencyTypeException() {
        super(ERROR_CODE);
    }
}
