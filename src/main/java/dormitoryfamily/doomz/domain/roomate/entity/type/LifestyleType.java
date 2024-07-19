package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidLifestyleTypeException;
import lombok.Getter;

@Getter
public enum LifestyleType {

    SLEEP_TIME(SleepTimeType.class), //수면시간
    WAKE_UP_TIME(WakeUpTimeType.class), //기상시간
    SLEEPING_HABIT(SleepingHabitType.class), //잠버릇
    SLEEPING_SENSITIVITY(SleepingSensitivityType.class), //잠귀
    SMOKING(SmokingType.class), //흡연 여부
    DRINKING_FREQUENCY(DrinkingFrequencyType.class), //음주 빈도
    SHOWER_TIME(ShowerTimeType.class), //샤워 시간대
    SHOWER_DURATION(ShowerDurationType.class), //샤워시간
    CLEANING_HABIT(CleaningFrequencyType.class), //청소
    HEAT_TOLERANCE(HeatToleranceType.class), //더위
    COLD_TOLERANCE(ColdToleranceType.class), //추위
    MBTI(MBTIType.class), //MBTI
    VISIT_HOME_FREQUENCY(VisitHomeFrequencyType.class), //본가가는 빈도
    LATE_NIGHT_SNACK(LateNightSnackType.class), //야식
    SNACK_IN_ROOM(SnackInRoomType.class), //야식 방안에서
    PHONE_SOUND(PhoneSoundType.class), //휴대폰 소리
    PERFUME_USAGE(PerfumeUsageType.class), //향수
    STUDY_LOCATION(StudyLocationType.class), //공부 장소
    EXAM_PREPARATION(ExamPreparationType.class), //시험
    EXERCISE(ExerciseType.class), //운동
    INSECT_TOLERANCE(InsectToleranceType.class); //벌레

    private final Class<? extends Enum<?>> enumClass;

    LifestyleType(Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;
    }

    public static LifestyleType fromType(String type) {
        for (LifestyleType lifestyleType : LifestyleType.values()) {
            if (lifestyleType.toString().equalsIgnoreCase(type)) {
                return lifestyleType;
            }
        }
        throw new InvalidLifestyleTypeException();
    }

    public Enum<?> getLifestyleValue(String value) {

        return switch (this) {
            case SLEEP_TIME -> SleepTimeType.fromDescription(value);
            case WAKE_UP_TIME -> WakeUpTimeType.fromDescription(value);
            case SLEEPING_HABIT -> SleepingHabitType.fromDescription(value);
            case SLEEPING_SENSITIVITY -> SleepingSensitivityType.fromDescription(value);
            case SMOKING -> SmokingType.fromDescription(value);
            case DRINKING_FREQUENCY -> DrinkingFrequencyType.fromDescription(value);
            case SHOWER_TIME -> ShowerTimeType.fromDescription(value);
            case SHOWER_DURATION -> ShowerDurationType.fromDescription(value);
            case CLEANING_HABIT -> CleaningFrequencyType.fromDescription(value);
            case HEAT_TOLERANCE -> HeatToleranceType.fromDescription(value);
            case COLD_TOLERANCE -> ColdToleranceType.fromDescription(value);
            case MBTI -> MBTIType.fromDescription(value);
            case VISIT_HOME_FREQUENCY -> VisitHomeFrequencyType.fromDescription(value);
            case LATE_NIGHT_SNACK -> LateNightSnackType.fromDescription(value);
            case SNACK_IN_ROOM -> SnackInRoomType.fromDescription(value);
            case PHONE_SOUND -> PhoneSoundType.fromDescription(value);
            case PERFUME_USAGE -> PerfumeUsageType.fromDescription(value);
            case STUDY_LOCATION -> StudyLocationType.fromDescription(value);
            case EXAM_PREPARATION -> ExamPreparationType.fromDescription(value);
            case EXERCISE -> ExerciseType.fromDescription(value);
            case INSECT_TOLERANCE -> InsectToleranceType.fromDescription(value);
            default -> null;
        };
    }
}
