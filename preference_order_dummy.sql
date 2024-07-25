USE dormitoryfamily;

-- 홍길동
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_홍길동(진리관)'), 'SLEEP_TIME', 'SleepTimeType:_2200', 1),
    ((SELECT member_id FROM member WHERE name = 'test_홍길동(진리관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 2),
    ((SELECT member_id FROM member WHERE name = 'test_홍길동(진리관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SNORING', 3),
    ((SELECT member_id FROM member WHERE name = 'test_홍길동(진리관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:MEDIUM', 4);

-- 김영희
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_김영희(정의관)'), 'SMOKING', 'SmokingType:NON_SMOKER', 1),
    ((SELECT member_id FROM member WHERE name = 'test_김영희(정의관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:OCCASIONAL', 2),
    ((SELECT member_id FROM member WHERE name = 'test_김영희(정의관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:IMMEDIATELY', 3),
    ((SELECT member_id FROM member WHERE name = 'test_김영희(정의관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 4);

-- 이철수
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_이철수(개척관)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 1),
    ((SELECT member_id FROM member WHERE name = 'test_이철수(개척관)'), 'EXAM_PREPARATION', 'ExamPreparationType:PREPARING', 2),
    ((SELECT member_id FROM member WHERE name = 'test_이철수(개척관)'), 'COLD_TOLERANCE', 'ColdToleranceType:HIGH', 3),
    ((SELECT member_id FROM member WHERE name = 'test_이철수(개척관)'), 'SLEEP_TIME', 'SleepTimeType:_2300', 4);

-- 박민수
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_박민수(계영원)'), 'SLEEP_TIME', 'SleepTimeType:_2400', 1),
    ((SELECT member_id FROM member WHERE name = 'test_박민수(계영원)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0800', 2),
    ((SELECT member_id FROM member WHERE name = 'test_박민수(계영원)'), 'SLEEPING_HABIT', 'SleepingHabitType:TEETH_GRINDING', 3),
    ((SELECT member_id FROM member WHERE name = 'test_박민수(계영원)'), 'COLD_TOLERANCE', 'ColdToleranceType:LOW', 4);

-- 정수진
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_정수진(지선관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 1),
    ((SELECT member_id FROM member WHERE name = 'test_정수진(지선관)'), 'PERFUME_USAGE', 'PerfumeUsageType:OFTEN', 2),
    ((SELECT member_id FROM member WHERE name = 'test_정수진(지선관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:REGULAR', 3),
    ((SELECT member_id FROM member WHERE name = 'test_정수진(지선관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0900', 4);

-- 오세진
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_오세진(명덕관)'), 'SMOKING', 'SmokingType:SMOKER', 1),
    ((SELECT member_id FROM member WHERE name = 'test_오세진(명덕관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SLEEP_TALKING', 2),
    ((SELECT member_id FROM member WHERE name = 'test_오세진(명덕관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:OCCASIONALLY', 3),
    ((SELECT member_id FROM member WHERE name = 'test_오세진(명덕관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0700', 4);

-- 이정환
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_이정환(신민관)'), 'SLEEP_TIME', 'SleepTimeType:BEFORE_2100', 1),
    ((SELECT member_id FROM member WHERE name = 'test_이정환(신민관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:BEFORE_0500', 2),
    ((SELECT member_id FROM member WHERE name = 'test_이정환(신민관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SNORING', 3),
    ((SELECT member_id FROM member WHERE name = 'test_이정환(신민관)'), 'COLD_TOLERANCE', 'ColdToleranceType:HIGH', 4);

-- 김민지
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_김민지(인의관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 1),
    ((SELECT member_id FROM member WHERE name = 'test_김민지(인의관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:NONE', 2),
    ((SELECT member_id FROM member WHERE name = 'test_김민지(인의관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:MEDIUM', 3),
    ((SELECT member_id FROM member WHERE name = 'test_김민지(인의관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_1000', 4);

-- 박지훈
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_박지훈(예지관)'), 'PERFUME_USAGE', 'PerfumeUsageType:NONE', 1),
    ((SELECT member_id FROM member WHERE name = 'test_박지훈(예지관)'), 'EXAM_PREPARATION', 'ExamPreparationType:PREPARING', 2),
    ((SELECT member_id FROM member WHERE name = 'test_박지훈(예지관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:ALL_AT_ONCE', 3),
    ((SELECT member_id FROM member WHERE name = 'test_박지훈(예지관)'), 'SLEEP_TIME', 'SleepTimeType:_0300', 4);

-- 강수진
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_강수진(등용관)'), 'SLEEP_TIME', 'SleepTimeType:_2100', 1),
    ((SELECT member_id FROM member WHERE name = 'test_강수진(등용관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:AFTER_1300', 2),
    ((SELECT member_id FROM member WHERE name = 'test_강수진(등용관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SNORING', 3),
    ((SELECT member_id FROM member WHERE name = 'test_강수진(등용관)'), 'COLD_TOLERANCE', 'ColdToleranceType:MEDIUM', 4);

-- 이민호
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_이민호(진리관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:FREQUENT', 1),
    ((SELECT member_id FROM member WHERE name = 'test_이민호(진리관)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 2),
    ((SELECT member_id FROM member WHERE name = 'test_이민호(진리관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:HIGH', 3),
    ((SELECT member_id FROM member WHERE name = 'test_이민호(진리관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 4);

-- 김하늘
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_김하늘(정의관)'), 'EXAM_PREPARATION', 'ExamPreparationType:PREPARING', 1),
    ((SELECT member_id FROM member WHERE name = 'test_김하늘(정의관)'), 'SLEEPING_HABIT', 'SleepingHabitType:TEETH_GRINDING', 2),
    ((SELECT member_id FROM member WHERE name = 'test_김하늘(정의관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 3),
    ((SELECT member_id FROM member WHERE name = 'test_김하늘(정의관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:LOW', 4);

-- 박철수
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_박철수(개척관)'), 'SLEEP_TIME', 'SleepTimeType:_2200', 1),
    ((SELECT member_id FROM member WHERE name = 'test_박철수(개척관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:OCCASIONAL', 2),
    ((SELECT member_id FROM member WHERE name = 'test_박철수(개척관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 3),
    ((SELECT member_id FROM member WHERE name = 'test_박철수(개척관)'), 'COLD_TOLERANCE', 'ColdToleranceType:LOW', 4);

-- 홍서연
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_홍서연(계영원)'), 'SLEEP_TIME', 'SleepTimeType:_2400', 1),
    ((SELECT member_id FROM member WHERE name = 'test_홍서연(계영원)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0900', 2),
    ((SELECT member_id FROM member WHERE name = 'test_홍서연(계영원)'), 'SLEEPING_HABIT', 'SleepingHabitType:NONE', 3),
    ((SELECT member_id FROM member WHERE name = 'test_홍서연(계영원)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:ALL_AT_ONCE', 4);

-- 최민수
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_최민수(지선관)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 1),
    ((SELECT member_id FROM member WHERE name = 'test_최민수(지선관)'), 'SLEEP_TIME', 'SleepTimeType:_0100', 2),
    ((SELECT member_id FROM member WHERE name = 'test_최민수(지선관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 3),
    ((SELECT member_id FROM member WHERE name = 'test_최민수(지선관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:REGULAR', 4);

-- 유리
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유리(명덕관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 1),
    ((SELECT member_id FROM member WHERE name = 'test_유리(명덕관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:LOW', 2),
    ((SELECT member_id FROM member WHERE name = 'test_유리(명덕관)'), 'COLD_TOLERANCE', 'ColdToleranceType:HIGH', 3),
    ((SELECT member_id FROM member WHERE name = 'test_유리(명덕관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:AFTER_1300', 4);

-- 민혁
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_민혁(신민관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SNORING', 1),
    ((SELECT member_id FROM member WHERE name = 'test_민혁(신민관)'), 'EXAM_PREPARATION', 'ExamPreparationType:NONE', 2),
    ((SELECT member_id FROM member WHERE name = 'test_민혁(신민관)'), 'PERFUME_USAGE', 'PerfumeUsageType:OFTEN', 3),
    ((SELECT member_id FROM member WHERE name = 'test_민혁(신민관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:FREQUENT', 4);

-- 민수
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_민수(인의관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:HIGH', 1),
    ((SELECT member_id FROM member WHERE name = 'test_민수(인의관)'), 'COLD_TOLERANCE', 'ColdToleranceType:LOW', 2),
    ((SELECT member_id FROM member WHERE name = 'test_민수(인의관)'), 'SLEEP_TIME', 'SleepTimeType:_2100', 3),
    ((SELECT member_id FROM member WHERE name = 'test_민수(인의관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0800', 4);

-- 소희
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_소희(예지관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 1),
    ((SELECT member_id FROM member WHERE name = 'test_소희(예지관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:OCCASIONAL', 2),
    ((SELECT member_id FROM member WHERE name = 'test_소희(예지관)'), 'SMOKING', 'SmokingType:NON_SMOKER', 3),
    ((SELECT member_id FROM member WHERE name = 'test_소희(예지관)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 4);

-- 진수
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_진수(등용관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:ALL_AT_ONCE', 1),
    ((SELECT member_id FROM member WHERE name = 'test_진수(등용관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 2),
    ((SELECT member_id FROM member WHERE name = 'test_진수(등용관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0900', 3),
    ((SELECT member_id FROM member WHERE name = 'test_진수(등용관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SLEEP_TALKING', 4);

-- 현주
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_현주(진리관)'), 'SLEEP_TIME', 'SleepTimeType:_2400', 1),
    ((SELECT member_id FROM member WHERE name = 'test_현주(진리관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0700', 2),
    ((SELECT member_id FROM member WHERE name = 'test_현주(진리관)'), 'COLD_TOLERANCE', 'ColdToleranceType:MEDIUM', 3),
    ((SELECT member_id FROM member WHERE name = 'test_현주(진리관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:LOW', 4);

-- 진혁
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_진혁(정의관)'), 'SLEEP_TIME', 'SleepTimeType:_2200', 1),
    ((SELECT member_id FROM member WHERE name = 'test_진혁(정의관)'), 'PERFUME_USAGE', 'PerfumeUsageType:OFTEN', 2),
    ((SELECT member_id FROM member WHERE name = 'test_진혁(정의관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:REGULAR', 3),
    ((SELECT member_id FROM member WHERE name = 'test_진혁(정의관)'), 'SLEEPING_HABIT', 'SleepingHabitType:TEETH_GRINDING', 4);

-- 가영
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_가영(개척관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 1),
    ((SELECT member_id FROM member WHERE name = 'test_가영(개척관)'), 'COLD_TOLERANCE', 'ColdToleranceType:HIGH', 2),
    ((SELECT member_id FROM member WHERE name = 'test_가영(개척관)'), 'SLEEP_TIME', 'SleepTimeType:_2100', 3),
    ((SELECT member_id FROM member WHERE name = 'test_가영(개척관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 4);

-- 민재
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_민재(계영원)'), 'SLEEPING_HABIT', 'SleepingHabitType:SLEEP_TALKING', 1),
    ((SELECT member_id FROM member WHERE name = 'test_민재(계영원)'), 'HEAT_TOLERANCE', 'HeatToleranceType:LOW', 2),
    ((SELECT member_id FROM member WHERE name = 'test_민재(계영원)'), 'COLD_TOLERANCE', 'ColdToleranceType:MEDIUM', 3),
    ((SELECT member_id FROM member WHERE name = 'test_민재(계영원)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0800', 4);

-- 서진
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_서진(지선관)'), 'EXAM_PREPARATION', 'ExamPreparationType:NONE', 1),
    ((SELECT member_id FROM member WHERE name = 'test_서진(지선관)'), 'SMOKING', 'SmokingType:SMOKER', 2),
    ((SELECT member_id FROM member WHERE name = 'test_서진(지선관)'), 'PERFUME_USAGE', 'PerfumeUsageType:NONE', 3),
    ((SELECT member_id FROM member WHERE name = 'test_서진(지선관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:OCCASIONAL', 4);

-- 수빈
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_수빈(명덕관)'), 'SLEEP_TIME', 'SleepTimeType:_2200', 1),
    ((SELECT member_id FROM member WHERE name = 'test_수빈(명덕관)'), 'SLEEPING_HABIT', 'SleepingHabitType:TEETH_GRINDING', 2),
    ((SELECT member_id FROM member WHERE name = 'test_수빈(명덕관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 3),
    ((SELECT member_id FROM member WHERE name = 'test_수빈(명덕관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:IMMEDIATELY', 4);

-- 준수
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_준수(신민관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:AFTER_1300', 1),
    ((SELECT member_id FROM member WHERE name = 'test_준수(신민관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:MEDIUM', 2),
    ((SELECT member_id FROM member WHERE name = 'test_준수(신민관)'), 'PERFUME_USAGE', 'PerfumeUsageType:OFTEN', 3),
    ((SELECT member_id FROM member WHERE name = 'test_준수(신민관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SNORING', 4);

-- 유진
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유진(인의관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:REGULAR', 1),
    ((SELECT member_id FROM member WHERE name = 'test_유진(인의관)'), 'SLEEP_TIME', 'SleepTimeType:_0200', 2),
    ((SELECT member_id FROM member WHERE name = 'test_유진(인의관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 3),
    ((SELECT member_id FROM member WHERE name = 'test_유진(인의관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 4);

-- 성민
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_성민(예지관)'), 'SLEEP_TIME', 'SleepTimeType:_2100', 1),
    ((SELECT member_id FROM member WHERE name = 'test_성민(예지관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SLEEP_TALKING', 2),
    ((SELECT member_id FROM member WHERE name = 'test_성민(예지관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:AFTER_1300', 3),
    ((SELECT member_id FROM member WHERE name = 'test_성민(예지관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:FREQUENT', 4);

-- 지훈
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_지훈(등용관)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 1),
    ((SELECT member_id FROM member WHERE name = 'test_지훈(등용관)'), 'COLD_TOLERANCE', 'ColdToleranceType:MEDIUM', 2),
    ((SELECT member_id FROM member WHERE name = 'test_지훈(등용관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:HIGH', 3),
    ((SELECT member_id FROM member WHERE name = 'test_지훈(등용관)'), 'SLEEPING_HABIT', 'SleepingHabitType:NONE', 4);

-- 예린
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_예린(진리관)'), 'SLEEP_TIME', 'SleepTimeType:_2300', 1),
    ((SELECT member_id FROM member WHERE name = 'test_예린(진리관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SLEEP_TALKING', 2),
    ((SELECT member_id FROM member WHERE name = 'test_예린(진리관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:OCCASIONALLY', 3),
    ((SELECT member_id FROM member WHERE name = 'test_예린(진리관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:OCCASIONAL', 4);

-- 수연
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_수연(정의관)'), 'SLEEPING_HABIT', 'SleepingHabitType:TEETH_GRINDING', 1),
    ((SELECT member_id FROM member WHERE name = 'test_수연(정의관)'), 'SLEEP_TIME', 'SleepTimeType:_2400', 2),
    ((SELECT member_id FROM member WHERE name = 'test_수연(정의관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 3),
    ((SELECT member_id FROM member WHERE name = 'test_수연(정의관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:LOW', 4);

-- 경민
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_경민(개척관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 1),
    ((SELECT member_id FROM member WHERE name = 'test_경민(개척관)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 2),
    ((SELECT member_id FROM member WHERE name = 'test_경민(개척관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:NONE', 3),
    ((SELECT member_id FROM member WHERE name = 'test_경민(개척관)'), 'SLEEP_TIME', 'SleepTimeType:_2100', 4);

-- 시윤
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_시윤(계영원)'), 'SLEEP_TIME', 'SleepTimeType:_2400', 1),
    ((SELECT member_id FROM member WHERE name = 'test_시윤(계영원)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0800', 2),
    ((SELECT member_id FROM member WHERE name = 'test_시윤(계영원)'), 'SLEEPING_HABIT', 'SleepingHabitType:TEETH_GRINDING', 3),
    ((SELECT member_id FROM member WHERE name = 'test_시윤(계영원)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:IMMEDIATELY', 4);

-- 다빈
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_다빈(지선관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 1),
    ((SELECT member_id FROM member WHERE name = 'test_다빈(지선관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:OCCASIONAL', 2),
    ((SELECT member_id FROM member WHERE name = 'test_다빈(지선관)'), 'PERFUME_USAGE', 'PerfumeUsageType:NONE', 3),
    ((SELECT member_id FROM member WHERE name = 'test_다빈(지선관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0800', 4);

-- 재현
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_재현(명덕관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:HIGH', 1),
    ((SELECT member_id FROM member WHERE name = 'test_재현(명덕관)'), 'COLD_TOLERANCE', 'ColdToleranceType:LOW', 2),
    ((SELECT member_id FROM member WHERE name = 'test_재현(명덕관)'), 'SLEEP_TIME', 'SleepTimeType:_2100', 3),
    ((SELECT member_id FROM member WHERE name = 'test_재현(명덕관)'), 'SLEEPING_HABIT', 'SleepingHabitType:NONE', 4);

-- 은지
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_은지(신민관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 1),
    ((SELECT member_id FROM member WHERE name = 'test_은지(신민관)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 2),
    ((SELECT member_id FROM member WHERE name = 'test_은지(신민관)'), 'COLD_TOLERANCE', 'ColdToleranceType:HIGH', 3),
    ((SELECT member_id FROM member WHERE name = 'test_은지(신민관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 4);

-- 유빈
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유빈(인의관)'), 'EXAM_PREPARATION', 'ExamPreparationType:NONE', 1),
    ((SELECT member_id FROM member WHERE name = 'test_유빈(인의관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:OCCASIONAL', 2),
    ((SELECT member_id FROM member WHERE name = 'test_유빈(인의관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:LOW', 3),
    ((SELECT member_id FROM member WHERE name = 'test_유빈(인의관)'), 'SLEEP_TIME', 'SleepTimeType:_2300', 4);

-- 태훈
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_태훈(예지관)'), 'SLEEP_TIME', 'SleepTimeType:_2200', 1),
    ((SELECT member_id FROM member WHERE name = 'test_태훈(예지관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0700', 2),
    ((SELECT member_id FROM member WHERE name = 'test_태훈(예지관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SNORING', 3),
    ((SELECT member_id FROM member WHERE name = 'test_태훈(예지관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:OCCASIONALLY', 4);

-- 도연
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_도연(등용관)'), 'SLEEP_TIME', 'SleepTimeType:_2300', 1),
    ((SELECT member_id FROM member WHERE name = 'test_도연(등용관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 2),
    ((SELECT member_id FROM member WHERE name = 'test_도연(등용관)'), 'SLEEPING_HABIT', 'SleepingHabitType:SLEEP_TALKING', 3),
    ((SELECT member_id FROM member WHERE name = 'test_도연(등용관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:IMMEDIATELY', 4);

-- 하영
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_하영(진리관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:HIGH', 1),
    ((SELECT member_id FROM member WHERE name = 'test_하영(진리관)'), 'SLEEPING_HABIT', 'SleepingHabitType:NONE', 2),
    ((SELECT member_id FROM member WHERE name = 'test_하영(진리관)'), 'SLEEP_TIME', 'SleepTimeType:_2100', 3),
    ((SELECT member_id FROM member WHERE name = 'test_하영(진리관)'), 'PERFUME_USAGE', 'PerfumeUsageType:OFTEN', 4);

-- 주연
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_주연(정의관)'), 'COLD_TOLERANCE', 'ColdToleranceType:LOW', 1),
    ((SELECT member_id FROM member WHERE name = 'test_주연(정의관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:LIGHT', 2),
    ((SELECT member_id FROM member WHERE name = 'test_주연(정의관)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:NONE', 3),
    ((SELECT member_id FROM member WHERE name = 'test_주연(정의관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0800', 4);

-- 하준
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_하준(개척관)'), 'PERFUME_USAGE', 'PerfumeUsageType:NONE', 1),
    ((SELECT member_id FROM member WHERE name = 'test_하준(개척관)'), 'SLEEP_TIME', 'SleepTimeType:_2400', 2),
    ((SELECT member_id FROM member WHERE name = 'test_하준(개척관)'), 'COLD_TOLERANCE', 'ColdToleranceType:MEDIUM', 3),
    ((SELECT member_id FROM member WHERE name = 'test_하준(개척관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0900', 4);

-- 나연
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_나연(계영원)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 1),
    ((SELECT member_id FROM member WHERE name = 'test_나연(계영원)'), 'PERFUME_USAGE', 'PerfumeUsageType:SOMETIMES', 2),
    ((SELECT member_id FROM member WHERE name = 'test_나연(계영원)'), 'DRINKING_FREQUENCY', 'DrinkingFrequencyType:REGULAR', 3),
    ((SELECT member_id FROM member WHERE name = 'test_나연(계영원)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0900', 4);

-- 유나
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유나(지선관)'), 'CLEANING_FREQUENCY', 'CleaningFrequencyType:ALL_AT_ONCE', 1),
    ((SELECT member_id FROM member WHERE name = 'test_유나(지선관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:LOW', 2),
    ((SELECT member_id FROM member WHERE name = 'test_유나(지선관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0800', 3),
    ((SELECT member_id FROM member WHERE name = 'test_유나(지선관)'), 'SLEEP_TIME', 'SleepTimeType:_2300', 4);

-- 재호
INSERT INTO preference_order (member_id, lifestyle_type, lifestyle_detail, preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_재호(명덕관)'), 'SLEEPING_SENSITIVITY', 'SleepingSensitivityType:DARK', 1),
    ((SELECT member_id FROM member WHERE name = 'test_재호(명덕관)'), 'HEAT_TOLERANCE', 'HeatToleranceType:HIGH', 2),
    ((SELECT member_id FROM member WHERE name = 'test_재호(명덕관)'), 'SLEEP_TIME', 'SleepTimeType:_0100', 3),
    ((SELECT member_id FROM member WHERE name = 'test_재호(명덕관)'), 'WAKE_UP_TIME', 'WakeUpTimeType:_0600', 4);
