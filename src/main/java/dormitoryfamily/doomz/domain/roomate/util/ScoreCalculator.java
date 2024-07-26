package dormitoryfamily.doomz.domain.roomate.util;

import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleAttribute;
import dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType;

import java.util.List;

import static dormitoryfamily.doomz.domain.roomate.entity.type.LifestyleType.getComparedLifestyleAttribute;
import static dormitoryfamily.doomz.domain.roomate.util.RoommateProperties.*;
import static dormitoryfamily.doomz.domain.roomate.util.ScoreProperties.*;

public class ScoreCalculator {

    /**
     * 사용자 한 명에 대한 점수 계산
     *
     * @param PreferenceOrders 우선순위들(1~4순위 포함)
     * @param lifestyle        비교할 사용자의 라이프 스타일
     */
    public static double calculateScoreForUser(List<PreferenceOrder> PreferenceOrders, Lifestyle lifestyle) {
        double userScore = 0;
        for (PreferenceOrder preferenceOrder : PreferenceOrders) {
            userScore += calculateScoreForOrder(preferenceOrder, lifestyle);
        }
        return userScore;
    }

    /**
     * 순위별 해당 항목에 대한 점수 계산
     *
     * @param preferenceOrder 우선순위 1개
     * @param lifestyle       비교할 사용자의 라이프 스타일
     */
    private static double calculateScoreForOrder(PreferenceOrder preferenceOrder, Lifestyle lifestyle) {
        LifestyleType preferredLifestyle = preferenceOrder.getLifestyleType();
        Integer order = preferenceOrder.getPreferenceOrder();

        LifestyleAttribute myStyle = (LifestyleAttribute) preferenceOrder.getLifestyleDetail();
        LifestyleAttribute anotherStyle = (LifestyleAttribute) getComparedLifestyleAttribute(preferredLifestyle, lifestyle);

        return applyWeightedScore(order, getScore(myStyle, anotherStyle));
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
