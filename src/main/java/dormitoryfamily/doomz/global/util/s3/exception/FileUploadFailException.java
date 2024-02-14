package dormitoryfamily.doomz.global.util.s3.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class FileUploadFailException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.FILE_UPLOAD_FAIL;

    public FileUploadFailException() {
        super(ERROR_CODE);
    }
}
