package dormitoryfamily.doomz.domain.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidMemberDormitoryTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_MEMBER_DORMITORY_TYPE;

    public InvalidMemberDormitoryTypeException() {
        super(ERROR_CODE);
    }
}