package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidVisitHomeFrequencyTypeException;
import lombok.Getter;

@Getter
public enum VisitHomeFrequencyType {

    RARELY("거의 안감"),
    EVERY_2_3_MONTHS("2,3달에 한 번"),
    EVERY_MONTH("1달에 한 번"),
    EVERY_WEEK("주에 한 번");

    private final String description;

    VisitHomeFrequencyType(String description) {
        this.description = description;
    }

    public static VisitHomeFrequencyType fromDescription(String description) {
        if(description == null) {
            return null;
        }

        for (VisitHomeFrequencyType type : VisitHomeFrequencyType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidVisitHomeFrequencyTypeException();
    }
}
