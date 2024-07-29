package dormitoryfamily.doomz.domain.roomate.exception.lifestyle;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class InvalidPhoneSoundTypeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.PHONE_SOUND_NOT_EXISTS;

    public InvalidPhoneSoundTypeException() {
        super(ERROR_CODE);
    }
}
