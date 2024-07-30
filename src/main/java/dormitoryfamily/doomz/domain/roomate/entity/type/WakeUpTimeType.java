package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidWakeUpTimeTypeException;
import lombok.Getter;

@Getter
public enum WakeUpTimeType implements LifestyleAttribute {

    BEFORE_0500("오전 5시 이전", 0),
    _0600("오전 6시", 1),
    _0700("오전 7시", 2),
    _0800("오전 8시", 3),
    _0900("오전 9시", 4),
    _1000("오전 10시", 5),
    _1100("오전 11시", 6),
    _1200("오후 12시", 7),
    AFTER_1300("오후 1시 이후", 8);

    private final String description;
    private final int index;

    WakeUpTimeType(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static WakeUpTimeType fromDescription(String description) {
        for (WakeUpTimeType type : WakeUpTimeType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidWakeUpTimeTypeException();
    }
}
