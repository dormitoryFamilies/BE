package dormitoryfamily.doomz.domain.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NotRoleMemberException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_ROLE_MEMBER;

    public NotRoleMemberException() {
        super(ERROR_CODE);
    }
}
