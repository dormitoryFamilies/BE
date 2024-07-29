package dormitoryfamily.doomz.domain.roomate.exception.lifestyle;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidHeatToleranceTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.HEAT_TOLERANCE_NOT_EXISTS;

    public InvalidHeatToleranceTypeException() {
        super(ERROR_CODE);
    }
}
