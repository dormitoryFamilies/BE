package dormitoryfamily.doomz.domain.chatRoom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class ChatRoomNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CHAT_ROOM_NOT_EXISTS;

    public ChatRoomNotExistsException() {
        super(ERROR_CODE);
    }
}

