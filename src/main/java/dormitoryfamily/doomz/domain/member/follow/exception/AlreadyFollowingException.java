package dormitoryfamily.doomz.domain.member.follow.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyFollowingException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_FOLLOW_MEMBER;

    public AlreadyFollowingException() {
        super(ERROR_CODE);
    }
}

