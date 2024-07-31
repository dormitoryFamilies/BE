package dormitoryfamily.doomz.domain.roomate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roomate.lifestyle.exception.InvalidStudyLocationException;
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
