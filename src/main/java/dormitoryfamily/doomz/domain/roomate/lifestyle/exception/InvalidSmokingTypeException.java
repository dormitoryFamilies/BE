package dormitoryfamily.doomz.domain.roomate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidSmokingTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SMOKING_NOT_EXISTS;

    public InvalidSmokingTypeException() {
        super(ERROR_CODE);
    }
}
