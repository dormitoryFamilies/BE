package dormitoryfamily.doomz.domain.member.follow.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NotFollowingMemberException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_IN_NOT_FOLLOWED;

    public NotFollowingMemberException() {
        super(ERROR_CODE);
    }
}

