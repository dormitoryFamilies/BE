package dormitoryfamily.doomz.domain.roommate.util;

import java.time.Duration;

/**
 * 룸메이트 추천 및 점수 계산 관련 상수 정의 클래스
 */
public class RoommateProperties {

    // ==========================
    //  선호도 우선순위
    // ==========================

    /** 1순위 */
    public static final int FIRST_PRIORITY = 1;

    /** 2순위 */
    public static final int SECOND_PRIORITY = 2;

    /** 3순위 */
    public static final int THIRD_PRIORITY = 3;

    /** 4순위 */
    public static final int FOURTH_PRIORITY = 4;

    // ==========================
    //  추천 관련 설정
    // ==========================

    /** 추천 후보 최대 수 */
    public static final int RECOMMENDATIONS_MAX_COUNT = 5;

    /** ANN 후보 풀 크기 (kNN으로 추출할 1차 후보 수) */
    public static final int ANN_CANDIDATE_POOL_SIZE = 100;

    /** Redis 키 접두사 */
    public static final String REDIS_CANDIDATES_KEY_PREFIX = "recommend:candidates:";

    /** 추천 캐시 유지 시간 (30분) */
    public static final Duration REDIS_CACHE_DURATION = Duration.ofMinutes(30);

    // ==========================
    //  점수 계산 기준
    // ==========================

    /** 점수 차이 최대값 기준 */
    public static final int MAX_SCORE_DIFFERENCE = 10;

    /** 기본 점수 (0점) */
    public static final double ZERO = 0.0;

    /** 가중치 값 **/
    public static final double FIRST_PRIORITY_WEIGHT = 1.0;
    public static final double SECOND_PRIORITY_WEIGHT = 0.7;
    public static final double THIRD_PRIORITY_WEIGHT = 0.5;
    public static final double FOURTH_PRIORITY_WEIGHT = 0.2;

    // ==========================
    //  Elasticsearch 인덱스 이름
    // ==========================

    /** 선호도 인덱스 이름 */
    public static final String PREFERENCE_INDEX = "preference";

    /** 라이프스타일 인덱스 이름 */
    public static final String LIFESTYLE_INDEX = "lifestyle";


    // ==========================
    //  Elasticsearch 문서 필드명
    // ==========================

    public static final String FIELD_PREFERENCE_WEIGHT = "preference_weight";
    public static final String FIELD_PREFERRED_VALUES = "preferred_values";
    public static final String FIELD_LIFESTYLE_VECTOR = "lifestyle_vector";

    // 인스턴스화 방지
    private RoommateProperties() {
        throw new UnsupportedOperationException("RoommateProperties는 유틸리티 클래스입니다.");
    }
}
