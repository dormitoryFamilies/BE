package dormitoryfamily.doomz.domain.member.member.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidDepartmentTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.DEPARTMENT_TYPE_NOT_EXISTS;

    public InvalidDepartmentTypeException() {
        super(ERROR_CODE);
    }
}
