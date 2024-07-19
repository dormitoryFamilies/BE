package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidSmokingTypeException;
import lombok.Getter;

@Getter
public enum SmokingType {

    NON_SMOKER("비흡연"),
    SMOKER("흡연");

    private final String description;

    SmokingType(String description) {
        this.description = description;
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
