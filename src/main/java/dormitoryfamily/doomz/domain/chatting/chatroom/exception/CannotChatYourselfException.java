package dormitoryfamily.doomz.domain.chatting.chatroom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class CannotChatYourselfException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CANNOT_CHAT_YOURSELF;

    public CannotChatYourselfException() {
        super(ERROR_CODE);
    }
}

