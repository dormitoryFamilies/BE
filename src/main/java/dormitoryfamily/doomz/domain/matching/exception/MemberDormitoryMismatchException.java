package dormitoryfamily.doomz.domain.matching.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MemberDormitoryMismatchException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_DORMITORY_MISMATCH;

    public MemberDormitoryMismatchException() {
        super(ERROR_CODE);
    }
}
