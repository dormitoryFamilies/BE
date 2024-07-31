package dormitoryfamily.doomz.domain.roomate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roomate.lifestyle.exception.InvalidHeatToleranceTypeException;
import lombok.Getter;

@Getter
public enum HeatToleranceType implements LifestyleAttribute {

    LOW("적게 탐", 0),
    MEDIUM("조금 탐", 4),
    HIGH("많이 탐", 8);

    private final String description;
    private final int index;

    HeatToleranceType(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static HeatToleranceType fromDescription(String description) {
        for (HeatToleranceType type : HeatToleranceType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidHeatToleranceTypeException();
    }
}
