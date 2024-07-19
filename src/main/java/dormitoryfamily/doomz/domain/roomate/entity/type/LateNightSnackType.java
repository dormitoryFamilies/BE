package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidLateNightSnackTypeException;
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
        if(description == null || description.trim().isEmpty()) {
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
