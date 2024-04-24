package dormitoryfamily.doomz.domain.chatRoom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyInChatRoomException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_IN_CHAT_ROOM;

    public AlreadyInChatRoomException() {
        super(ERROR_CODE);
    }
}