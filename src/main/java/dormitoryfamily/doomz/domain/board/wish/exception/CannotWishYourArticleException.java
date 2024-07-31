package dormitoryfamily.doomz.domain.board.wish.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class CannotWishYourArticleException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CANNOT_WISH_YOUR_ARTICLE;

    public CannotWishYourArticleException() {
        super(ERROR_CODE);
    }
}
