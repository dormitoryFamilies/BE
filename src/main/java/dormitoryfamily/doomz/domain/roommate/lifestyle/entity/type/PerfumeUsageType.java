package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidPerfumeUsageTypeException;
import lombok.Getter;

@Getter
public enum PerfumeUsageType implements LifestyleAttribute {

    NONE("미사용", 0),
    SOMETIMES("가끔", 5),
    OFTEN("자주", 8);

    private final String description;
    private final int index;

    PerfumeUsageType(String description, int index) {
        this.description = description;
        this.index = index;
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
