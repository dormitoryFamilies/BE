package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidSleepingTimeTypeException;
import lombok.Getter;

@Getter
public enum SleepTimeType implements Describable {

    BEFORE_2100("오후 9시 이전"),
    _2100("오후 9시"),
    _2200("오후 10시"),
    _2300("오후 11시"),
    _2400("오전 12시"),
    _0100("오전 1시"),
    _0200("오전 2시"),
    _0300("오전 3시"),
    AFTER_0300("오전 3시 이후");

    private final String description;

    SleepTimeType(String description) {
        this.description = description;
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
