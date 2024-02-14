package dormitoryfamily.doomz.global.util.s3.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class FileMaxSizeExceededException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MAX_SIZE_EXCEEDED;

    public FileMaxSizeExceededException() {
        super(ERROR_CODE);
    }
}
