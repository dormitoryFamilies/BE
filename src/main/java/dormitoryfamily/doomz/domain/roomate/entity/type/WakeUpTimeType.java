package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidWakeUpTimeTypeException;
import lombok.Getter;

@Getter
public enum WakeUpTimeType {

    BEFORE_0400("오전 4시 이전"),
    _0400("오전 4시"),
    _0500("오전 5시"),
    _0600("오전 6시"),
    _0700("오전 7시"),
    _0800("오전 8시"),
    _0900("오전 9시"),
    _1000("오전 10시"),
    AFTER_1000("오전 10시 이후");

    private final String description;

    WakeUpTimeType(String description) {
        this.description = description;
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
