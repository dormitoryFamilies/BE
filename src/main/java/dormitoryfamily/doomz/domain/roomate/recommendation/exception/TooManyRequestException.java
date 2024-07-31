package dormitoryfamily.doomz.domain.roomate.recommendation.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class TooManyRequestException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.TOO_MANY_REQUEST;
    private final long remainingMinutes;

    public TooManyRequestException(long remainingMinutes) {
        super(ERROR_CODE);
        this.remainingMinutes = remainingMinutes;
    }
}
