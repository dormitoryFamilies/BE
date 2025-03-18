package dormitoryfamily.doomz.domain.roommate.matching.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MatchingInterruptException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INTERRUPTED;
    public MatchingInterruptException() {
        super(ERROR_CODE);
    }
}
