package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidSleepingHabitTypeException;
import lombok.Getter;

@Getter
public enum SleepingHabitType implements LifestyleAttribute {

    TEETH_GRINDING("이갈이", 9),
    SNORING("코골이", 9),
    SLEEP_TALKING("잠꼬대", 9),
    NONE("없음", 1);

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
