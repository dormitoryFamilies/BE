package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidColdToleranceTypeException;
import lombok.Getter;

@Getter
public enum ColdToleranceType implements LifestyleAttribute {

    LOW("적게 탐", 0),
    MEDIUM("조금 탐", 4),
    HIGH("많이 탐", 8);

    private final String description;
    private final int index;

    ColdToleranceType(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static ColdToleranceType fromDescription(String description) {
        for (ColdToleranceType type : ColdToleranceType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidColdToleranceTypeException();
    }
}
