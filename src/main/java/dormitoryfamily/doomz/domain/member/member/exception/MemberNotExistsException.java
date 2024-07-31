package dormitoryfamily.doomz.domain.member.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MemberNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_NOT_EXISTS;

    public MemberNotExistsException() {
        super(ERROR_CODE);
    }
}
