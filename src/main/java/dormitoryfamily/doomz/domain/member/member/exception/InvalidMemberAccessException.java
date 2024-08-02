package dormitoryfamily.doomz.domain.member.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidMemberAccessException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_MEMBER_ACCESS;

    public InvalidMemberAccessException() {
        super(ERROR_CODE);
    }
}
