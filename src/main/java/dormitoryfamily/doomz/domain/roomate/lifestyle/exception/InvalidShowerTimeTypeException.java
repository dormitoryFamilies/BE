package dormitoryfamily.doomz.domain.roomate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidShowerTimeTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SHOWER_TIME_NOT_EXISTS;

    public InvalidShowerTimeTypeException() {
        super(ERROR_CODE);
    }
}
