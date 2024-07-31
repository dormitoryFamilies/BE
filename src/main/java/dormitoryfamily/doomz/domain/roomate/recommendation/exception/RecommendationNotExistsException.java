package dormitoryfamily.doomz.domain.roomate.recommendation.exception;

import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;

public class RecommendationNotExistsException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.RECOMMENDATION_NOT_EXISTS;

    public RecommendationNotExistsException() {
        super(ERROR_CODE);
    }
}
