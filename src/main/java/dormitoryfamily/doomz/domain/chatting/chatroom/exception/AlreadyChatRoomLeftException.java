package dormitoryfamily.doomz.domain.chatting.chatroom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyChatRoomLeftException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_CHAT_ROOM_LEFT;

    public AlreadyChatRoomLeftException() {
        super(ERROR_CODE);
    }
}