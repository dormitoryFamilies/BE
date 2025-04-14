package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidDrinkingFrequencyTypeException;
import lombok.Getter;

@Getter
public enum DrinkingFrequencyType implements LifestyleAttribute {

    NONE("없음", 1),
    OCCASIONAL("가끔", 4),
    FREQUENT("종종", 6),
    REGULAR("자주", 9);

    private final String description;
    private final int index;

    DrinkingFrequencyType(String description, int index) {
        this.description = description;
        this.index = index;
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
