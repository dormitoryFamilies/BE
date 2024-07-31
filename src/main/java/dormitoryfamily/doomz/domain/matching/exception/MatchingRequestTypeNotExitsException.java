package dormitoryfamily.doomz.domain.matching.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MatchingRequestTypeNotExitsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MATCHING_REQUEST_STATUS_NOT_EXISTS;

    public MatchingRequestTypeNotExitsException() {
        super(ERROR_CODE);
    }
}
