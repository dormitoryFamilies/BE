package dormitoryfamily.doomz.domain.roomate.lifestyle.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidStudyLocationException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.STUDY_LOCATION_NOT_EXISTS;

    public InvalidStudyLocationException() {
        super(ERROR_CODE);
    }
}
