package dormitoryfamily.doomz.domain.roomate.entity.type;

import lombok.Getter;

@Getter
public enum LifestyleType {
    SLEEPING_TIME(SleepTimeType.class),
    WAKE_UP_TIME(WakeUpTimeType.class),
    SMOKING(SmokingType.class),
    EXERCISE(ExerciseType.class);

    private final Class<? extends Enum<?>> enumClass;

    LifestyleType(Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;
    }

    public static LifestyleType fromString(String str) {
        for (LifestyleType lifestyleType : LifestyleType.values()) {
            if (lifestyleType.toString().equals(str)) {
                return lifestyleType;
            }
        }
        return null;
    }

    public Enum<?> getEnumValue(String value) {
        if (this.enumClass == CleaningHabitType.class) {
            return CleaningHabitType.fromDescription(value);
        } else if (this.enumClass == ExerciseType.class) {
            return ExerciseType.fromDescription(value);
        } else if (this.enumClass == SleepTimeType.class) {
            return SleepTimeType.fromDescription(value);
        } else if (this.enumClass == SmokingType.class) {
            return SmokingType.fromDescription(value);
        }
        return null;
    }
}
