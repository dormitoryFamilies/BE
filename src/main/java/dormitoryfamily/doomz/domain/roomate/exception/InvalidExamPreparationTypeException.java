package dormitoryfamily.doomz.domain.roomate.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidExamPreparationTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EXAM_PREPARATION_NOT_EXISTS;

    public InvalidExamPreparationTypeException() {
        super(ERROR_CODE);
    }
}
