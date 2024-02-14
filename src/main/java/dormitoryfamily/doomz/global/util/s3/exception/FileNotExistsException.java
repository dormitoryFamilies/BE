package dormitoryfamily.doomz.global.util.s3.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class FileNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.FILE_NOT_EXISTS;

    public FileNotExistsException() {
        super(ERROR_CODE);
    }
}
