package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicatePreferenceOrderException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_REGISTER_PREFERENCE_ORDER;
    private final String duplicatePreference;

    public DuplicatePreferenceOrderException(String duplicatePreference) {
        super(ERROR_CODE);
        this.duplicatePreference = duplicatePreference;
    }
}
