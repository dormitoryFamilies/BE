package dormitoryfamily.doomz.global.security.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MemberDataNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_ACCESS_TOKEN;

    public MemberDataNotExistsException() {
        super(ERROR_CODE);
    }
}
