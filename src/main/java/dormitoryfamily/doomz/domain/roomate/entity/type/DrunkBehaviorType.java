package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidDrinkingFrequencyTypeException;
import lombok.Getter;

@Getter
public enum DrunkBehaviorType {

    NONE("없음"),
    OCCASIONALLY("가끔"),
    FREQUENTLY("종종"),
    REGULARLY("자주");

    private final String description;

    DrunkBehaviorType(String description) {
        this.description = description;
    }

    public static DrunkBehaviorType fromDescription(String description) {
        for (DrunkBehaviorType type : DrunkBehaviorType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidDrinkingFrequencyTypeException();
    }
}
