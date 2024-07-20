package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidHeatToleranceTypeException;
import lombok.Getter;

@Getter
public enum HeatToleranceType implements Describable {

    LOW("적게 탐"),
    MEDIUM("조금 탐"),
    HIGH("많이 탐");

    private final String description;

    HeatToleranceType(String description) {
        this.description = description;
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
