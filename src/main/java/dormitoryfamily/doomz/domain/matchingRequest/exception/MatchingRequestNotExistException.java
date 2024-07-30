package dormitoryfamily.doomz.domain.matchingRequest.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MatchingRequestNotExistException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MATCHING_REQUEST_NOT_EXISTS;

    public MatchingRequestNotExistException() {
        super(ERROR_CODE);
    }
}

