package dormitoryfamily.doomz.domain.roomatewish.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyWishedRoommateException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_WISHED_ROOMMATE;

    public AlreadyWishedRoommateException() {
        super(ERROR_CODE);
    }
}
