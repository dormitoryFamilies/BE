package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidDrinkingFrequencyTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.DRINKING_FREQUENCY_NOT_EXISTS;

    public InvalidDrinkingFrequencyTypeException() {
        super(ERROR_CODE);
    }
}
