package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.member.exception.InvalidCollegeTypeException;
import lombok.Getter;

@Getter
public enum CleaningHabitType {

    IMMEDIATELY("바로바로"),
    OCCASIONALLY("가끔"),
    BATCH("몰아서");

    private final String description;

    CleaningHabitType(String description) {
        this.description = description;
    }

    public static CleaningHabitType fromDescription(String description) {
        for (CleaningHabitType type : CleaningHabitType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidCollegeTypeException();
    }

    public CleaningHabitType abc(String str) {
        for (CleaningHabitType type : CleaningHabitType.values()) {
            if (type.description.equals(str)) {
                return type;
            }
        }
        throw new InvalidCollegeTypeException();
    }
}
