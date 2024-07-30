package dormitoryfamily.doomz.domain.matchingRequest.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class CannotMatchingYourselfException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CANNOT_MATCHING_YOURSELF;

    public CannotMatchingYourselfException() {
        super(ERROR_CODE);
    }
}

