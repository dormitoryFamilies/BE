package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidSleepingTimeTypeException;
import lombok.Getter;

@Getter
public enum SleepTimeType implements LifestyleAttribute {

    BEFORE_2100("오후 9시 이전", 0),
    _2100("오후 9시", 1),
    _2200("오후 10시", 2),
    _2300("오후 11시", 3),
    _2400("오전 12시", 4),
    _0100("오전 1시", 5),
    _0200("오전 2시", 6),
    _0300("오전 3시", 7),
    AFTER_0300("오전 3시 이후", 8);

    private final String description;
    private final int index;

    SleepTimeType(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static SleepTimeType fromDescription(String description) {
        for (SleepTimeType type : SleepTimeType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidSleepingTimeTypeException();
    }
}
