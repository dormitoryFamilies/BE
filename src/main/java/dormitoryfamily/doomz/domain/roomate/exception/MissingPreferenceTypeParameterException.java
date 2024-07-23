package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class MissingPreferenceTypeParameterException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MISSING_REQUIRED_TYPE_PARAMETER;
    private final String requiredType;

    public MissingPreferenceTypeParameterException(String requiredType) {
        super(ERROR_CODE);
        this.requiredType = requiredType;
    }
}
