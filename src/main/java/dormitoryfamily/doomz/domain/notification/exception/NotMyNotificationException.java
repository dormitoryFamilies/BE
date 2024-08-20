package dormitoryfamily.doomz.domain.notification.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotMyNotificationException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_MY_NOTIFICATION;
    private final Long wrongId;

    public NotMyNotificationException(Long wrongId) {
        super(ERROR_CODE);
        this.wrongId = wrongId;
    }
}
