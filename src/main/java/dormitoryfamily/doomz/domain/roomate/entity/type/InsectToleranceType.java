package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidInsectToleranceTypeException;
import lombok.Getter;

@Getter
public enum InsectToleranceType {

    EXPERT("잘잡아요"),
    SMALL_ONLY("작은것만"),
    CANNOT("못잡아요");

    private final String description;

    InsectToleranceType(String description) {
        this.description = description;
    }

    public static InsectToleranceType fromDescription(String description) {
        if(description == null || description.trim().isEmpty()) {
            return null;
        }

        for (InsectToleranceType type : InsectToleranceType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidInsectToleranceTypeException();
    }
}
