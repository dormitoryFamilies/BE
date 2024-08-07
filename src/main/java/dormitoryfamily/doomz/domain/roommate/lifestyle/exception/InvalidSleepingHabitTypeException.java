package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidSleepingHabitTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SLEEPING_HABIT_NOT_EXISTS;

    public InvalidSleepingHabitTypeException() {
        super(ERROR_CODE);
    }
}
