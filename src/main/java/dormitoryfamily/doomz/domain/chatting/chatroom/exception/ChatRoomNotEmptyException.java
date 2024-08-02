package dormitoryfamily.doomz.domain.chatting.chatroom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class ChatRoomNotEmptyException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CHAT_ROOM_NOT_EMPTY;

    public ChatRoomNotEmptyException() {
        super(ERROR_CODE);
    }
}
