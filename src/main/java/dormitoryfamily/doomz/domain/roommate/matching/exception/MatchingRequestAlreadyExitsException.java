package dormitoryfamily.doomz.domain.roommate.matching.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MatchingRequestAlreadyExitsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_MATCHING_REQUEST_EXISTS;

    public MatchingRequestAlreadyExitsException() {
        super(ERROR_CODE);
    }
}

