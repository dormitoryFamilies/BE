package dormitoryfamily.doomz.global.util.s3.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class FileEmptyException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.FILE_EMPTY;

    public FileEmptyException() {
        super(ERROR_CODE);
    }
}
