package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidCleaningHabitTypeException;
import lombok.Getter;

@Getter
public enum CleaningFrequencyType implements LifestyleAttribute {

    IMMEDIATELY("바로바로", 1),
    OCCASIONALLY("가끔", 5),
    ALL_AT_ONCE("몰아서", 9);

    private final String description;
    private final int index;

    CleaningFrequencyType(String description, int index) {
        this.description = description;
        this.index = index;
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
