package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidSleepingSensitivityTypeException;
import lombok.Getter;

@Getter
public enum SleepingSensitivityType implements Describable {

    DARK("어두움"),
    LIGHT("밝음");

    private final String description;

    SleepingSensitivityType(String description) {
        this.description = description;
    }

    public static SleepingSensitivityType fromDescription(String description) {
        for (SleepingSensitivityType type : SleepingSensitivityType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidSleepingSensitivityTypeException();
    }
}
