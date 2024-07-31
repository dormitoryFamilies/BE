USE dormitory_family;

-- 홍길동
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_홍길동(진리관)'),
     'SleepTimeType:_2200',
     'WakeUpTimeType:_0600',
     'SleepingHabitType:SNORING',
     'HeatToleranceType:MEDIUM');

-- 김영희
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_김영희(정의관)'),
     'SmokingType:NON_SMOKER',
     'DrinkingFrequencyType:OCCASIONAL',
     'CleaningFrequencyType:IMMEDIATELY',
     'SleepingSensitivityType:LIGHT');

-- 이철수
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_이철수(개척관)'),
     'PerfumeUsageType:SOMETIMES',
     'ExamPreparationType:PREPARING',
     'ColdToleranceType:HIGH',
     'SleepTimeType:_2300');

-- 박민수
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_박민수(계영원)'),
     'SleepTimeType:_2400',
     'WakeUpTimeType:_0800',
     'SleepingHabitType:TEETH_GRINDING',
     'ColdToleranceType:LOW');

-- 정수진
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_정수진(지선관)'),
     'SleepingSensitivityType:DARK',
     'PerfumeUsageType:OFTEN',
     'DrinkingFrequencyType:REGULAR',
     'WakeUpTimeType:_0900');

-- 오세진
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_오세진(명덕관)'),
     'SmokingType:SMOKER',
     'SleepingHabitType:SLEEP_TALKING',
     'CleaningFrequencyType:OCCASIONALLY',
     'WakeUpTimeType:_0700');

-- 이정환
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_이정환(신민관)'),
     'SleepTimeType:BEFORE_2100',
     'WakeUpTimeType:BEFORE_0600',
     'SleepingHabitType:SNORING',
     'ColdToleranceType:HIGH');

-- 김민지
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_김민지(인의관)'),
     'SleepingSensitivityType:DARK',
     'DrinkingFrequencyType:NONE',
     'HeatToleranceType:MEDIUM',
     'WakeUpTimeType:_1000');

-- 박지훈
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_박지훈(예지관)'),
     'PerfumeUsageType:NONE',
     'ExamPreparationType:PREPARING',
     'CleaningFrequencyType:ALL_AT_ONCE',
     'SleepTimeType:_0300');

-- 강수진
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_강수진(등용관)'),
     'SleepTimeType:_2100',
     'WakeUpTimeType:AFTER_1200',
     'SleepingHabitType:SNORING',
     'ColdToleranceType:MEDIUM');

-- 이민호
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_이민호(진리관)'),
     'DrinkingFrequencyType:FREQUENT',
     'PerfumeUsageType:SOMETIMES',
     'HeatToleranceType:HIGH',
     'WakeUpTimeType:_0600');

-- 김하늘
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_김하늘(정의관)'),
     'ExamPreparationType:PREPARING',
     'SleepingHabitType:TEETH_GRINDING',
     'WakeUpTimeType:_0600',
     'HeatToleranceType:LOW');

-- 박철수
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_박철수(개척관)'),
     'SleepTimeType:_2200',
     'DrinkingFrequencyType:OCCASIONAL',
     'SleepingSensitivityType:DARK',
     'ColdToleranceType:LOW');

-- 홍서연
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_홍서연(계영원)'),
     'SleepTimeType:_2400',
     'WakeUpTimeType:_0900',
     'SleepingHabitType:NONE',
     'CleaningFrequencyType:ALL_AT_ONCE');

-- 최민수
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_최민수(지선관)'),
     'PerfumeUsageType:SOMETIMES',
     'SleepTimeType:_0100',
     'WakeUpTimeType:_0600',
     'DrinkingFrequencyType:REGULAR');

-- 유리
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유리(명덕관)'),
     'SleepingSensitivityType:LIGHT',
     'HeatToleranceType:LOW',
     'ColdToleranceType:HIGH',
     'WakeUpTimeType:AFTER_1200');

-- 민혁
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_민혁(신민관)'),
     'SleepingHabitType:SNORING',
     'ExamPreparationType:NONE',
     'PerfumeUsageType:OFTEN',
     'DrinkingFrequencyType:FREQUENT');

-- 민수
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_민수(인의관)'),
     'HeatToleranceType:HIGH',
     'ColdToleranceType:LOW',
     'SleepTimeType:_2100',
     'WakeUpTimeType:_0800');

-- 소희
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_소희(예지관)'),
     'SleepingSensitivityType:DARK',
     'DrinkingFrequencyType:OCCASIONAL',
     'SmokingType:NON_SMOKER',
     'PerfumeUsageType:SOMETIMES');

-- 진수
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_진수(등용관)'),
     'CleaningFrequencyType:ALL_AT_ONCE',
     'SleepingSensitivityType:LIGHT',
     'WakeUpTimeType:_0900',
     'SleepingHabitType:SLEEP_TALKING');

-- 현주
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_현주(진리관)'),
     'SleepTimeType:_2400',
     'WakeUpTimeType:_0700',
     'ColdToleranceType:MEDIUM',
     'HeatToleranceType:LOW');

-- 진혁
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_진혁(정의관)'),
     'SleepTimeType:_2200',
     'PerfumeUsageType:OFTEN',
     'DrinkingFrequencyType:REGULAR',
     'SleepingHabitType:TEETH_GRINDING');

-- 가영
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_가영(개척관)'),
     'SleepingSensitivityType:DARK',
     'ColdToleranceType:HIGH',
     'SleepTimeType:_2100',
     'WakeUpTimeType:_0600');

-- 민재
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_민재(계영원)'),
     'SleepingHabitType:SLEEP_TALKING',
     'HeatToleranceType:LOW',
     'ColdToleranceType:MEDIUM',
     'WakeUpTimeType:_0800');

-- 서진
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_서진(지선관)'),
     'ExamPreparationType:NONE',
     'SmokingType:SMOKER',
     'PerfumeUsageType:NONE',
     'DrinkingFrequencyType:OCCASIONAL');

-- 수빈
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_수빈(명덕관)'),
     'SleepTimeType:_2200',
     'SleepingHabitType:TEETH_GRINDING',
     'SleepingSensitivityType:LIGHT',
     'CleaningFrequencyType:IMMEDIATELY');

-- 준수
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_준수(신민관)'),
     'WakeUpTimeType:AFTER_1200',
     'HeatToleranceType:MEDIUM',
     'PerfumeUsageType:OFTEN',
     'SleepingHabitType:SNORING');

-- 유진
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유진(인의관)'),
     'DrinkingFrequencyType:REGULAR',
     'SleepTimeType:_0200',
     'WakeUpTimeType:_0600',
     'SleepingSensitivityType:DARK');

-- 성민
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_성민(예지관)'),
     'SleepTimeType:_2100',
     'SleepingHabitType:SLEEP_TALKING',
     'WakeUpTimeType:AFTER_1200',
     'DrinkingFrequencyType:FREQUENT');

-- 지훈
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_지훈(등용관)'),
     'PerfumeUsageType:SOMETIMES',
     'ColdToleranceType:MEDIUM',
     'HeatToleranceType:HIGH',
     'SleepingHabitType:NONE');

-- 예린
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_예린(진리관)'),
     'SleepTimeType:_2300',
     'SleepingHabitType:SLEEP_TALKING',
     'CleaningFrequencyType:OCCASIONALLY',
     'DrinkingFrequencyType:OCCASIONAL');

-- 수연
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_수연(정의관)'),
     'SleepingHabitType:TEETH_GRINDING',
     'SleepTimeType:_2400',
     'WakeUpTimeType:_0600',
     'HeatToleranceType:LOW');

-- 경민
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_경민(개척관)'),
     'SleepingSensitivityType:LIGHT',
     'PerfumeUsageType:SOMETIMES',
     'DrinkingFrequencyType:NONE',
     'SleepTimeType:_2100');

-- 시윤
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_시윤(계영원)'),
     'SleepTimeType:_2400',
     'WakeUpTimeType:_0800',
     'SleepingHabitType:TEETH_GRINDING',
     'CleaningFrequencyType:IMMEDIATELY');

-- 다빈
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_다빈(지선관)'),
     'SleepingSensitivityType:LIGHT',
     'DrinkingFrequencyType:OCCASIONAL',
     'PerfumeUsageType:NONE',
     'WakeUpTimeType:_0800');

-- 재현
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_재현(명덕관)'),
     'HeatToleranceType:HIGH',
     'ColdToleranceType:LOW',
     'SleepTimeType:_2100',
     'SleepingHabitType:NONE');

-- 은지
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_은지(신민관)'),
     'SleepingSensitivityType:LIGHT',
     'PerfumeUsageType:SOMETIMES',
     'ColdToleranceType:HIGH',
     'WakeUpTimeType:_0600');

-- 유빈
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유빈(인의관)'),
     'ExamPreparationType:NONE',
     'DrinkingFrequencyType:OCCASIONAL',
     'HeatToleranceType:LOW',
     'SleepTimeType:_2300');

-- 태훈
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_태훈(예지관)'),
     'SleepTimeType:_2200',
     'WakeUpTimeType:_0700',
     'SleepingHabitType:SNORING',
     'CleaningFrequencyType:OCCASIONALLY');

-- 도연
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_도연(등용관)'),
     'SleepTimeType:_2300',
     'WakeUpTimeType:_0600',
     'SleepingHabitType:SLEEP_TALKING',
     'CleaningFrequencyType:IMMEDIATELY');

-- 하영
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_하영(진리관)'),
     'HeatToleranceType:HIGH',
     'SleepingHabitType:NONE',
     'SleepTimeType:_2100',
     'PerfumeUsageType:OFTEN');

-- 주연
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_주연(정의관)'),
     'ColdToleranceType:LOW',
     'SleepingSensitivityType:LIGHT',
     'DrinkingFrequencyType:NONE',
     'WakeUpTimeType:_0800');

-- 하준
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_하준(개척관)'),
     'PerfumeUsageType:NONE',
     'SleepTimeType:_2400',
     'ColdToleranceType:MEDIUM',
     'WakeUpTimeType:_0900');

-- 나연
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_나연(계영원)'),
     'SleepingSensitivityType:DARK',
     'PerfumeUsageType:SOMETIMES',
     'DrinkingFrequencyType:REGULAR',
     'WakeUpTimeType:_0900');

-- 유나
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_유나(지선관)'),
     'CleaningFrequencyType:ALL_AT_ONCE',
     'HeatToleranceType:LOW',
     'WakeUpTimeType:_0800',
     'SleepTimeType:_2300');

-- 재호
INSERT INTO preference_order (member_id, first_preference_order, second_preference_order, third_preference_order, fourth_preference_order)
VALUES
    ((SELECT member_id FROM member WHERE name = 'test_재호(명덕관)'),
     'SleepingSensitivityType:DARK',
     'HeatToleranceType:HIGH',
     'SleepTimeType:_0100',
     'WakeUpTimeType:_0600');

