package dormitoryfamily.doomz.domain.follow.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class CannotFollowYourselfException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CANNOT_FOLLOW_YOURSELF;

    public CannotFollowYourselfException() {
        super(ERROR_CODE);
    }
}
