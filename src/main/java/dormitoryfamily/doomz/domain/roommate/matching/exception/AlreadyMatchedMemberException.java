package dormitoryfamily.doomz.domain.roommate.matching.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyMatchedMemberException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_MATCHED_MEMBER;

    public AlreadyMatchedMemberException() {
        super(ERROR_CODE);
    }
}