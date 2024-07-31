package dormitoryfamily.doomz.domain.board.replycomment.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class ReplyCommentNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.REPLY_COMMENT_NOT_EXISTS;

    public ReplyCommentNotExistsException() {
        super(ERROR_CODE);
    }
}
