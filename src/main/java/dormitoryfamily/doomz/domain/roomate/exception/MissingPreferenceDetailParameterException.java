package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class MissingPreferenceDetailParameterException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MISSING_REQUIRED_DETAIL_PARAMETER;
    private final String requiredValue;

    public MissingPreferenceDetailParameterException(String requiredValue) {
        super(ERROR_CODE);
        this.requiredValue = requiredValue;
    }
}
