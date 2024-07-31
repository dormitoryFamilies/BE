package dormitoryfamily.doomz.domain.board.comment.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class CommentIsDeletedException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.COMMENT_IS_DELETED;

    public CommentIsDeletedException() {
        super(ERROR_CODE);
    }
}
