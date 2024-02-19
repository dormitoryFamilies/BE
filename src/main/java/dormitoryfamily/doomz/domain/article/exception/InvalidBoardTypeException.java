package dormitoryfamily.doomz.domain.article.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidBoardTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.BOARD_TYPE_NOT_EXISTS;

    public InvalidBoardTypeException() {
        super(ERROR_CODE);
    }
}
