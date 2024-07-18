package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.member.exception.InvalidCollegeTypeException;
import lombok.Getter;


@Getter
public enum ExerciseType {

    NO("안해요"),
    AT_DORMITORY("긱사에서"),
    AT_GYM("헬스장에서");

    private final String description;

    ExerciseType(String description) {
        this.description = description;
    }

    public static ExerciseType fromDescription(String description) {
        for (ExerciseType type : ExerciseType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidCollegeTypeException();
    }
}
