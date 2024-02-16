package dormitoryfamily.doomz.domain.article.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class BoardTypeNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.BOARD_TYPE_NOT_EXISTS;

    public BoardTypeNotExistsException() {
        super(ERROR_CODE);
    }
}
