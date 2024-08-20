package dormitoryfamily.doomz.domain.notification.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotificationAlreadyReadException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_READ_NOTIFICATION;
    private final Long alreadyReadNotificationId;

    public NotificationAlreadyReadException(Long alreadyReadNotificationId) {
        super(ERROR_CODE);
        this.alreadyReadNotificationId = alreadyReadNotificationId;
    }
}
