package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.lifestyle.InvalidSleepingSensitivityTypeException;
import lombok.Getter;

@Getter
public enum SleepingSensitivityType implements LifestyleAttribute {

    DARK("어두움", 0),
    LIGHT("밝음", 8);

    private final String description;
    private final int index;

    SleepingSensitivityType(String description, int index) {
        this.description = description;
        this.index = index;
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
