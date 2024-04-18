package dormitoryfamily.doomz.domain.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NicknameDuplicatedException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NICKNAME_DUPLICATED;

    public NicknameDuplicatedException() {
        super(ERROR_CODE);
    }
}
