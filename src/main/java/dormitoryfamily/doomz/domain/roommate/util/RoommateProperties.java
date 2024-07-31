package dormitoryfamily.doomz.domain.roommate.util;

public class RoommateProperties {
    /**
     * 선호 우선순위
     */
    public static final int FIRST_PRIORITY = 1;
    public static final int SECOND_PRIORITY = 2;
    public static final int THIRD_PRIORITY = 3;
    public static final int FOURTH_PRIORITY = 4;

    /**
     * 추천 가능 시간 간격
     */
    public static final int RECOMMENDATION_INTERVAL_HOURS = 24;

    /**
     * 룸메 추천
     */
    public static final int RECOMMENDATIONS_MAX_COUNT = 50;

    /**
     * 총점 계산 기준
     */
    public static final Integer MAX_SCORE_DIFFERENCE = 9;

    /**
     * 가중치 값
     */
    public static final double FIRST_PRIORITY_WEIGHT = 1.0;
    public static final double SECOND_PRIORITY_WEIGHT = 0.7;
    public static final double THIRD_PRIORITY_WEIGHT = 0.5;
    public static final double FOURTH_PRIORITY_WEIGHT = 0.2;
}
