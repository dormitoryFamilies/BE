package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidLifestyleTypeException;
import lombok.Getter;

@Getter
public enum LifestyleType {

    SLEEP_TIME("sleepTime", SleepTimeType.class, 0),
    WAKE_UP_TIME("wakeUpTime", WakeUpTimeType.class, 1),
    SLEEPING_HABIT("sleepingHabit", SleepingHabitType.class, 2),
    SLEEPING_SENSITIVITY("sleepingSensitivity", SleepingSensitivityType.class, 3),
    SMOKING("smoking", SmokingType.class, 4),
    DRINKING_FREQUENCY("drinkingFrequency", DrinkingFrequencyType.class, 5),
    CLEANING_FREQUENCY("cleaningFrequency", CleaningFrequencyType.class, 6),
    HEAT_TOLERANCE("heatTolerance", HeatToleranceType.class, 7),
    COLD_TOLERANCE("coldTolerance", ColdToleranceType.class, 8),
    PERFUME_USAGE("perfumeUsage", PerfumeUsageType.class, 9),
    EXAM_PREPARATION("examPreparation", ExamPreparationType.class, 10),
    // 나머지는 벡터에 포함 안되니까 인덱스 -1
    SHOWER_TIME("showerTime", ShowerTimeType.class, -1),
    SHOWER_DURATION("showerDuration", ShowerDurationType.class, -1),
    MBTI("MBTI", MBTIType.class, -1),
    VISIT_HOME_FREQUENCY("visitHomeFrequency", VisitHomeFrequencyType.class, -1),
    LATE_NIGHT_SNACK("lateNightSnack", LateNightSnackType.class, -1),
    SNACK_IN_ROOM("snackInRoom", SnackInRoomType.class, -1),
    PHONE_SOUND("phoneSound", PhoneSoundType.class, -1),
    STUDY_LOCATION("studyLocation", StudyLocationType.class, -1),
    EXERCISE("exercise", ExerciseType.class, -1),
    INSECT_TOLERANCE("insectTolerance", InsectToleranceType.class, -1);

    private final String type;
    private final Class<? extends Enum<?>> enumClass;
    private final int vectorIndex;

    LifestyleType(String type, Class<? extends Enum<?>> enumClass, int vectorIndex) {
        this.type = type;
        this.enumClass = enumClass;
        this.vectorIndex = vectorIndex;
    }

    public static LifestyleType fromType(String typeStr) {
        for (LifestyleType lifestyleType : LifestyleType.values()) {
            if (lifestyleType.name().equalsIgnoreCase(typeStr)) {
                return lifestyleType;
            }
        }
        throw new InvalidLifestyleTypeException(typeStr);
    }

    public static LifestyleType fromTypeName(String simpleName) {
        for (LifestyleType lifestyleType : LifestyleType.values()) {
            if (lifestyleType.enumClass.getSimpleName().equals(simpleName)) {
                return lifestyleType;
            }
        }
        throw new InvalidLifestyleTypeException(simpleName);
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

    public boolean isEssential() {
        return switch (this) {
            case SLEEP_TIME, WAKE_UP_TIME, SMOKING, SLEEPING_HABIT, SLEEPING_SENSITIVITY, DRINKING_FREQUENCY,
                    CLEANING_FREQUENCY, HEAT_TOLERANCE, COLD_TOLERANCE, PERFUME_USAGE, EXAM_PREPARATION -> true;
            default -> false;
        };
    }
}
