package dormitoryfamily.doomz.domain.member.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidMemberDormitoryTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_DORMITORY_TYPE_NOT_EXISTS;

    public InvalidMemberDormitoryTypeException() {
        super(ERROR_CODE);
    }
}