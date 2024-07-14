package dormitoryfamily.doomz.domain.chatRoom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyEnteredChatRoomException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_ENTERED_CHAT_ROOM;

    public AlreadyEnteredChatRoomException() {
        super(ERROR_CODE);
    }
}