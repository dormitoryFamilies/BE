package dormitoryfamily.doomz.domain.roommate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidSnackInRoomTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SNACK_IN_ROOM_NOT_EXISTS;

    public InvalidSnackInRoomTypeException() {
        super(ERROR_CODE);
    }
}
