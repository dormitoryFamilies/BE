package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidLateNightSnackTypeException;
import lombok.Getter;

@Getter
public enum LateNightSnackType {

    NEVER("안 먹어요"),
    SOMETIMES("가끔"),
    OFTEN("자주");

    private final String description;

    LateNightSnackType(String description) {
        this.description = description;
    }

    public static LateNightSnackType fromDescription(String description) {
        if(description == null) {
            return null;
        }

        for (LateNightSnackType type : LateNightSnackType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidLateNightSnackTypeException();
    }
}
