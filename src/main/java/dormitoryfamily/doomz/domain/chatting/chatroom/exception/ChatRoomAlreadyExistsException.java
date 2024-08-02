package dormitoryfamily.doomz.domain.chatting.chatroom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class ChatRoomAlreadyExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_CHAT_ROOM_EXISTS;

    public ChatRoomAlreadyExistsException() {
        super(ERROR_CODE);
    }
}
