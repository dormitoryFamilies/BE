package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidDrinkingFrequencyTypeException;
import lombok.Getter;

@Getter
public enum DrinkingFrequencyType {

    NONE("없음"),
    OCCASIONAL("가끔"),
    FREQUENT("종종"),
    REGULAR("자주");

    private final String description;

    DrinkingFrequencyType(String description) {
        this.description = description;
    }

    public static DrinkingFrequencyType fromDescription(String description) {
        for (DrinkingFrequencyType type : DrinkingFrequencyType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidDrinkingFrequencyTypeException();
    }
}
