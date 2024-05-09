package dormitoryfamily.doomz.domain.chat.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidChatMessageException  extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CHAT_SINGLE_CONTENT_REQUIRED;

    public InvalidChatMessageException() {
        super(ERROR_CODE);
    }
}
