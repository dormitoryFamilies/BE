package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roomate.exception.lifestyle.InvalidLifestyleTypeException;
import lombok.Getter;

@Getter
public enum LifestyleType {

    SLEEP_TIME("sleepTime", SleepTimeType.class), //수면시간
    WAKE_UP_TIME("wakeUpTime", WakeUpTimeType.class), //기상시간
    SLEEPING_HABIT("sleepingHabit", SleepingHabitType.class), //잠버릇
    SLEEPING_SENSITIVITY("sleepingSensitivity", SleepingSensitivityType.class), //잠귀
    SMOKING("smoking", SmokingType.class), //흡연 여부
    DRINKING_FREQUENCY("drinkingFrequency", DrinkingFrequencyType.class), //음주 빈도
    SHOWER_TIME("showerTime", ShowerTimeType.class), //샤워 시간대
    SHOWER_DURATION("showerDuration", ShowerDurationType.class), //샤워시간
    CLEANING_FREQUENCY("cleaningFrequency", CleaningFrequencyType.class), //청소
    HEAT_TOLERANCE("heatTolerance", HeatToleranceType.class), //더위
    COLD_TOLERANCE("coldTolerance", ColdToleranceType.class), //추위
    MBTI("MBTI", MBTIType.class), //MBTI
    VISIT_HOME_FREQUENCY("visitHomeFrequency", VisitHomeFrequencyType.class), //본가가는 빈도
    LATE_NIGHT_SNACK("lateNightSnack", LateNightSnackType.class), //야식
    SNACK_IN_ROOM("snackInRoom", SnackInRoomType.class), //야식 방안에서
    PHONE_SOUND("phoneSound", PhoneSoundType.class), //휴대폰 소리
    PERFUME_USAGE("perfumeUsage", PerfumeUsageType.class), //향수
    STUDY_LOCATION("studyLocation", StudyLocationType.class), //공부 장소
    EXAM_PREPARATION("examPreparation", ExamPreparationType.class), //시험
    EXERCISE("exercise", ExerciseType.class), //운동
    INSECT_TOLERANCE("insectTolerance", InsectToleranceType.class); //벌레

    private final String type;
    private final Class<? extends Enum<?>> enumClass;

    LifestyleType(String type, Class<? extends Enum<?>> enumClass) {
        this.type = type;
        this.enumClass = enumClass;
    }

    public static LifestyleType fromType(String typeStr) {
        for (LifestyleType lifestyleType : LifestyleType.values()) {
            if (lifestyleType.toString().equalsIgnoreCase(typeStr)) {
                return lifestyleType;
            }
        }
        throw new InvalidLifestyleTypeException(typeStr);
    }

    public Enum<?> getLifestyleValueFrom(String value) {
        return switch (this) {
            case SLEEP_TIME -> SleepTimeType.fromDescription(value);
            case WAKE_UP_TIME -> WakeUpTimeType.fromDescription(value);
            case SLEEPING_HABIT -> SleepingHabitType.fromDescription(value);
            case SLEEPING_SENSITIVITY -> SleepingSensitivityType.fromDescription(value);
            case SMOKING -> SmokingType.fromDescription(value);
            case DRINKING_FREQUENCY -> DrinkingFrequencyType.fromDescription(value);
            case SHOWER_TIME -> ShowerTimeType.fromDescription(value);
            case SHOWER_DURATION -> ShowerDurationType.fromDescription(value);
            case CLEANING_FREQUENCY -> CleaningFrequencyType.fromDescription(value);
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

    public static String getRequiredEnumType(String lifestyleType) {
        return switch (lifestyleType) {
            case "SleepTimeType" -> "SLEEP_TIME";
            case "WakeUpTimeType" -> "WAKE_UP_TIME";
            case "SleepingHabitType" -> "SLEEPING_HABIT";
            case "SleepingSensitivityType" -> "SLEEPING_SENSITIVITY";
            case "SmokingType" -> "SMOKING";
            case "DrinkingFrequencyType" -> "DRINKING_FREQUENCY";
            case "CleaningFrequencyType" -> "CLEANING_FREQUENCY";
            case "HeatToleranceType" -> "HEAT_TOLERANCE";
            case "ColdToleranceType" -> "COLD_TOLERANCE";
            case "PerfumeUsageType" -> "PERFUME_USAGE";
            case "ExamPreparationType" -> "EXAM_PREPARATION";
            default -> null;
        };
    }

    public static LifestyleAttribute getTargetLifestyleAttribute(LifestyleType preferredType, Lifestyle lifestyle) {
        return switch (preferredType) {
            case SLEEP_TIME -> lifestyle.getSleepTimeType();
            case WAKE_UP_TIME -> lifestyle.getWakeUpTimeType();
            case SMOKING -> lifestyle.getSmokingType();
            case SLEEPING_HABIT -> lifestyle.getSleepingHabitType();
            case SLEEPING_SENSITIVITY -> lifestyle.getSleepingSensitivityType();
            case DRINKING_FREQUENCY -> lifestyle.getDrinkingFrequencyType();
            case CLEANING_FREQUENCY -> lifestyle.getCleaningFrequencyType();
            case HEAT_TOLERANCE -> lifestyle.getHeatToleranceType();
            case COLD_TOLERANCE -> lifestyle.getColdToleranceType();
            case PERFUME_USAGE -> lifestyle.getPerfumeUsageType();
            case EXAM_PREPARATION -> lifestyle.getExamPreparationType();
            default -> null;
        };
    }
}
