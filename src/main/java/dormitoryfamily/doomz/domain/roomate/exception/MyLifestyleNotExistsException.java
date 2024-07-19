package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MyLifestyleNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MY_LIFESTYLE_NOT_EXISTS;

    public MyLifestyleNotExistsException() {
        super(ERROR_CODE);
    }
}
