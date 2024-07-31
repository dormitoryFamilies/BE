package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidMBTITypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MBTI_NOT_EXISTS;

    public InvalidMBTITypeException() {
        super(ERROR_CODE);
    }
}
