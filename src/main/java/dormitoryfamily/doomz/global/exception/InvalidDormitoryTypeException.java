package dormitoryfamily.doomz.global.exception;

public class InvalidDormitoryTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.DORMITORY_TYPE_NOT_EXISTS;

    public InvalidDormitoryTypeException() {
        super(ERROR_CODE);
    }
}
