package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidSleepingHabitTypeException;
import lombok.Getter;

@Getter
public enum SleepingHabitType implements LifestyleAttribute {

    TEETH_GRINDING("이갈이", 8),
    SNORING("코골이", 8),
    SLEEP_TALKING("잠꼬대", 8),
    NONE("없음", 0);

    private final String description;
    private final int index;

    SleepingHabitType(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static SleepingHabitType fromDescription(String description) {
        for (SleepingHabitType type : SleepingHabitType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidSleepingHabitTypeException();
    }
}
