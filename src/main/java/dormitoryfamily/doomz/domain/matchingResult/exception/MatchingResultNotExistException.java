package dormitoryfamily.doomz.domain.matchingResult.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MatchingResultNotExistException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MATCHING_RESULT_NOT_EXISTS;

    public MatchingResultNotExistException() {
        super(ERROR_CODE);
    }
}
