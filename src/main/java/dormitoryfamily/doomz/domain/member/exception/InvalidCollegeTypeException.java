package dormitoryfamily.doomz.domain.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidCollegeTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.COLLEGE_TYPE_NOT_EXISTS;

    public InvalidCollegeTypeException() {
        super(ERROR_CODE);
    }
}
