package dormitoryfamily.doomz.domain.chatting.chatroom.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class MemberNotInChatRoomException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_NOT_IN_CHAT_ROOM;

    public MemberNotInChatRoomException() {
        super(ERROR_CODE);
    }
}
