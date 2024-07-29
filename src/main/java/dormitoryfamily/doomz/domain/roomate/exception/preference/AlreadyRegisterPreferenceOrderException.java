package dormitoryfamily.doomz.domain.roomate.exception.preference;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyRegisterPreferenceOrderException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_REGISTER_PREFERENCE_ORDER;

    public AlreadyRegisterPreferenceOrderException() {
        super(ERROR_CODE);
    }
}
