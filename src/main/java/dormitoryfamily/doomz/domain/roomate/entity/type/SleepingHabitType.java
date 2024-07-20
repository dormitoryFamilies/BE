package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidSleepingHabitTypeException;
import lombok.Getter;

@Getter
public enum SleepingHabitType implements Describable {

    TEETH_GRINDING("이갈이"),
    SNORING("코골이"),
    SLEEP_TALKING("잠꼬대"),
    NONE("없음");

    private final String description;

    SleepingHabitType(String description) {
        this.description = description;
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
