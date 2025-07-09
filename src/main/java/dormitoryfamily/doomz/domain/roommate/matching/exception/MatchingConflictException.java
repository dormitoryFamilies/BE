package dormitoryfamily.doomz.domain.roommate.matching.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MatchingConflictException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MATCHING_CONFLICT;

    public MatchingConflictException() {
        super(ERROR_CODE);
    }
}
