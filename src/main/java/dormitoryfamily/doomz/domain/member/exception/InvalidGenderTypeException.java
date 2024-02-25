package dormitoryfamily.doomz.domain.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidGenderTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.GENDER_TYPE_NOT_EXISTS;

    public InvalidGenderTypeException() {
        super(ERROR_CODE);
    }
}