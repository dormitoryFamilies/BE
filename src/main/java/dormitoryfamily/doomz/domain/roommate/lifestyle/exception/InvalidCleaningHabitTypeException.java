package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidCleaningHabitTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CLEANING_HABIT_NOT_EXISTS;

    public InvalidCleaningHabitTypeException() {
        super(ERROR_CODE);
    }
}
