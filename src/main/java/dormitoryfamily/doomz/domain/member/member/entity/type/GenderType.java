package dormitoryfamily.doomz.domain.member.member.entity.type;

import dormitoryfamily.doomz.domain.member.member.exception.InvalidGenderTypeException;
import lombok.Getter;

@Getter
public enum GenderType {

    MALE("남자"),
    FEMALE("여자");

    private final String description;

    GenderType(String description) {
        this.description = description;
    }

    public static GenderType fromDescription(String description) {
        for (GenderType type : GenderType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidGenderTypeException();
    }
}
