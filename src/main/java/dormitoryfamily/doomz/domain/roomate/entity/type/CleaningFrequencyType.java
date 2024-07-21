package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.member.exception.InvalidCollegeTypeException;
import dormitoryfamily.doomz.domain.roomate.exception.InvalidCleaningHabitTypeException;
import lombok.Getter;

@Getter
public enum CleaningFrequencyType implements Describable {

    IMMEDIATELY("바로바로"),
    OCCASIONALLY("가끔"),
    ALL_AT_ONCE("몰아서");

    private final String description;

    CleaningFrequencyType(String description) {
        this.description = description;
    }

    public static CleaningFrequencyType fromDescription(String description) {
        for (CleaningFrequencyType type : CleaningFrequencyType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidCleaningHabitTypeException();
    }
}
