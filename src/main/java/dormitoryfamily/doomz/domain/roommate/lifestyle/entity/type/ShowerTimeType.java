package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidShowerTimeTypeException;
import lombok.Getter;

@Getter
public enum ShowerTimeType {

    MORNING("아침"),
    EVENING("저녁");

    private final String description;

    ShowerTimeType(String description) {
        this.description = description;
    }

    public static ShowerTimeType fromDescription(String description) {
        if(description == null) {
            return null;
        }

        for (ShowerTimeType type : ShowerTimeType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidShowerTimeTypeException();
    }
}
