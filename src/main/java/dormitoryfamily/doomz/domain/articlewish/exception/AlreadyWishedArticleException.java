package dormitoryfamily.doomz.domain.articlewish.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class AlreadyWishedArticleException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALREADY_WISHED_ARTICLE;

    public AlreadyWishedArticleException() {
        super(ERROR_CODE);
    }
}
