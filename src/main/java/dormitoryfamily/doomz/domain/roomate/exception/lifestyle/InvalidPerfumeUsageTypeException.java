package dormitoryfamily.doomz.domain.roomate.exception.lifestyle;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidPerfumeUsageTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.PERFUME_USAGE_NOT_EXISTS;

    public InvalidPerfumeUsageTypeException() {
        super(ERROR_CODE);
    }
}
