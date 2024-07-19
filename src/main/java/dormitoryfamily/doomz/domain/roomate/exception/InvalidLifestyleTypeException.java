package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidLifestyleTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.LIFESTYLE_NOT_EXISTS;
    private final String value;

    public InvalidLifestyleTypeException(String value) {
        super(ERROR_CODE);
        this.value = value;
    }
}
