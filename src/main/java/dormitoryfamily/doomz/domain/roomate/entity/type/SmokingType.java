package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.member.exception.InvalidCollegeTypeException;
import lombok.Getter;

@Getter
public enum SmokingType {
    NONE("없음"),
    SOMETIMES("가끔"),
    OCCASIONALLY("종종"),
    OFTEN("자주");

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
        throw new InvalidCollegeTypeException();
    }
}
