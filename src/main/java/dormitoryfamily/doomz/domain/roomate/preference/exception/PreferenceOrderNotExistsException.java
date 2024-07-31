package dormitoryfamily.doomz.domain.roomate.preference.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class PreferenceOrderNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.PREFERENCE_ORDER_NOT_EXISTS;

    public PreferenceOrderNotExistsException() {
        super(ERROR_CODE);
    }
}
