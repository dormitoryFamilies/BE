package dormitoryfamily.doomz.domain.board.comment.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class CommentNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.COMMENT_NOT_EXISTS;

    public CommentNotExistsException() {
        super(ERROR_CODE);
    }
}
