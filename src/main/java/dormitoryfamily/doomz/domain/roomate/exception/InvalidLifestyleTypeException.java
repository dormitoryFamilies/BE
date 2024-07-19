package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidLifestyleTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.LIFE_STYLE_NOT_EXISTS;

    public InvalidLifestyleTypeException() {
        super(ERROR_CODE);
    }
}
