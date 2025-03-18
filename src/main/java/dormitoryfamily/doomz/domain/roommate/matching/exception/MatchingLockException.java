package dormitoryfamily.doomz.domain.roommate.matching.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MatchingLockException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MATCHING_LOCK;

    public MatchingLockException() {
        super(ERROR_CODE);
    }
}
