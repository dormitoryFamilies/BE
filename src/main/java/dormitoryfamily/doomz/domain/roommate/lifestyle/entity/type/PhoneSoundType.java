package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidPhoneSoundTypeException;
import lombok.Getter;

@Getter
public enum PhoneSoundType {

    EARPHONES("이어폰"),
    SPEAKER("스피커"),
    VARIABLE("유동적");

    private final String description;

    PhoneSoundType(String description) {
        this.description = description;
    }

    public static PhoneSoundType fromDescription(String description) {
        if(description == null || description.isEmpty()) {
            return null;
        }

        for (PhoneSoundType type : PhoneSoundType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidPhoneSoundTypeException();
    }
}
