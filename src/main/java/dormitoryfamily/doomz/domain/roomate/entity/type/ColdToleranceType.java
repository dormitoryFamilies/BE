package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidColdToleranceTypeException;
import lombok.Getter;

@Getter
public enum ColdToleranceType {

    LOW("적게 탐"),
    MEDIUM("조금 탐"),
    HIGH("많이 탐");

    private final String description;

    ColdToleranceType(String description) {
        this.description = description;
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
