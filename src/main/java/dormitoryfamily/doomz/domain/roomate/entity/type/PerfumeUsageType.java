package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidPerfumeUsageTypeException;
import lombok.Getter;

@Getter
public enum PerfumeUsageType implements Describable {

    NONE("미사용"),
    SOMETIMES("가끔"),
    OFTEN("자주");

    private final String description;

    PerfumeUsageType(String description) {
        this.description = description;
    }

    public static PerfumeUsageType fromDescription(String description) {
        for (PerfumeUsageType type : PerfumeUsageType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidPerfumeUsageTypeException();
    }
}
