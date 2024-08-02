package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidStudyLocationException;
import lombok.Getter;

@Getter
public enum StudyLocationType {

    DORMITORY("기숙사"),
    OFF_DORMITORY("기숙사 외"),
    VARIABLE("유동적");

    private final String description;

    StudyLocationType(String description) {
        this.description = description;
    }

    public static StudyLocationType fromDescription(String description) {
        if(description == null) {
            return null;
        }

        for (StudyLocationType type : StudyLocationType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidStudyLocationException();
    }
}
