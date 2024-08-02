package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidSmokingTypeException;
import lombok.Getter;

@Getter
public enum SmokingType implements LifestyleAttribute {

    NON_SMOKER("비흡연", 0),
    SMOKER("흡연", 8);

    private final String description;
    private final int index;

    SmokingType(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static SmokingType fromDescription(String description) {
        for (SmokingType type : SmokingType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidSmokingTypeException();
    }
}
