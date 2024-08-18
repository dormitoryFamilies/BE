package dormitoryfamily.doomz.domain.notification.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotificationNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOTIFICATION_NOT_EXISTS;
    private final Long notExistsId;

    public NotificationNotExistsException(Long notExistsId) {
        super(ERROR_CODE);
        this.notExistsId = notExistsId;
    }
}
