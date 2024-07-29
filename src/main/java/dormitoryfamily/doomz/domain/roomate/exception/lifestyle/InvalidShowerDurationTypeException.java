package dormitoryfamily.doomz.domain.roomate.exception.lifestyle;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidShowerDurationTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SHOWER_DURATION_NOT_EXISTS;

    public InvalidShowerDurationTypeException() {
        super(ERROR_CODE);
    }
}
