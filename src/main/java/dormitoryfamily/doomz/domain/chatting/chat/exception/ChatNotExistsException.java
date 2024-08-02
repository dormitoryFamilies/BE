package dormitoryfamily.doomz.domain.chatting.chat.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class ChatNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CHAT_NOT_EXISTS;

    public ChatNotExistsException() {
        super(ERROR_CODE);
    }
}
