package dormitoryfamily.doomz.domain.member.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NotVisitorOrRejectedMemberRoleException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_ROLE_VISITOR_OR_REJECTED_MEMBER;

    public NotVisitorOrRejectedMemberRoleException() {
        super(ERROR_CODE);
    }
}
