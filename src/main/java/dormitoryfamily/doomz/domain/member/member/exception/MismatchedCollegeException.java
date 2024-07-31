package dormitoryfamily.doomz.domain.member.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MismatchedCollegeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.COLLEGE_DEPARTMENT_MISMATCH;

    public MismatchedCollegeException() {
        super(ERROR_CODE);
    }
}