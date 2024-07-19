package dormitoryfamily.doomz.domain.articleWish.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class NotWishedArticleException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_IS_NOT_WISHED;

    public NotWishedArticleException() {
        super(ERROR_CODE);
    }
}
