package dormitoryfamily.doomz.domain.roommate.util;

import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleAttribute;

import static dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type.LifestyleType.*;
import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.*;

public class ScoreCalculator {

    /**
     * 사용자 한 명에 대한 점수 계산
     *
     * @param preferenceOrder 우선순위(1~4순위)
     * @param lifestyle       비교할 라이프 스타일
     */
    public static double calculateScoreForUser(PreferenceOrder preferenceOrder, Lifestyle lifestyle) {
        double userScore = 0;

        userScore += calculateScoreForOrder(preferenceOrder.getFirstPreferenceOrder(), lifestyle, FIRST_PRIORITY);
        userScore += calculateScoreForOrder(preferenceOrder.getSecondPreferenceOrder(), lifestyle, SECOND_PRIORITY);
        userScore += calculateScoreForOrder(preferenceOrder.getThirdPreferenceOrder(), lifestyle, THIRD_PRIORITY);
        userScore += calculateScoreForOrder(preferenceOrder.getFourthPreferenceOrder(), lifestyle, FOURTH_PRIORITY);

        return userScore;
    }

    /**
     * 순위별 해당 항목에 대한 점수 계산
     *
     * @param preferredLifestyle 연산 대상의 우선순위
     * @param lifestyle          비교할 라이프 스타일
     */
    private static double calculateScoreForOrder(Enum<?> preferredLifestyle, Lifestyle lifestyle, int order) {
        LifestyleAttribute baseLifestyle = (LifestyleAttribute) preferredLifestyle;
        LifestyleAttribute targetLifestyle = getTargetLifestyle(baseLifestyle, lifestyle);

        return applyWeightedScore(order, getScore(baseLifestyle, targetLifestyle));
    }

    private static LifestyleAttribute getTargetLifestyle(LifestyleAttribute baseLifestyle, Lifestyle lifestyle) {
        String lifestyleEnumType = getRequiredEnumType(baseLifestyle.getClass().getSimpleName()); //enum 타입명을 토대로 enum 상수명을 조회
        return getTargetLifestyleAttribute(fromType(lifestyleEnumType), lifestyle);       //라이프 스타일에서 조회하고자 하는 lifestyle enum 조회
    }

    private static int getScore(LifestyleAttribute myStyle, LifestyleAttribute anotherStyle) {
        return MAX_SCORE_DIFFERENCE - Math.abs(myStyle.getIndex() - anotherStyle.getIndex());
    }

    /**
     * 항목에 대한 가중치 계산
     *
     * @param order 순위
     * @param score 점수
     * @return 순위별 가중치가 계산된 총점
     */
    private static double applyWeightedScore(int order, int score) {
        return switch (order) {
            case FIRST_PRIORITY -> score * FIRST_PRIORITY_WEIGHT;
            case SECOND_PRIORITY -> score * SECOND_PRIORITY_WEIGHT;
            case THIRD_PRIORITY -> score * THIRD_PRIORITY_WEIGHT;
            case FOURTH_PRIORITY -> score * FOURTH_PRIORITY_WEIGHT;
            default -> score;
        };
    }
}
