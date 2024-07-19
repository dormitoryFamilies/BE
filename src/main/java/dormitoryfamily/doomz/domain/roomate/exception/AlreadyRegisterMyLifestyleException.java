package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyRegisterMyLifestyleException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_REGISTER_MY_LIFESTYLE;

    public AlreadyRegisterMyLifestyleException() {
        super(ERROR_CODE);
    }
}
