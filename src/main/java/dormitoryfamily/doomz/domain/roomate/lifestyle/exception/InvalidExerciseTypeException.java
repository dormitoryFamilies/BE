package dormitoryfamily.doomz.domain.roomate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidExerciseTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EXERCISE_NOT_EXISTS;

    public InvalidExerciseTypeException() {
        super(ERROR_CODE);
    }
}
