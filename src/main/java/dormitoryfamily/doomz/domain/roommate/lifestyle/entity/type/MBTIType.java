package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidMBTITypeException;
import lombok.Getter;

@Getter
public enum MBTIType {

    ISTJ, ISFJ, INFJ, INTJ,
    ISTP, ISFP, INFP, INTP,
    ESTP, ESFP, ENFP, ENTP,
    ESTJ, ESFJ, ENFJ, ENTJ;

    public static MBTIType fromDescription(String description) {
        if(description == null || description.isEmpty()) {
            return null;
        }

        for (MBTIType type : MBTIType.values()) {
            if (type.toString().equals(description)) {
                return type;
            }
        }
        throw new InvalidMBTITypeException();
    }
}
