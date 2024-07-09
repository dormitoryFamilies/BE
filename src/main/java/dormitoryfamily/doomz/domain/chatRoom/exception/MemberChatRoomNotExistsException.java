package dormitoryfamily.doomz.domain.chatRoom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MemberChatRoomNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_CHAT_ROOM_NOT_EXISTS;

    public MemberChatRoomNotExistsException () {
        super(ERROR_CODE);
    }
}
