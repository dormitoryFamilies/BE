package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidExerciseTypeException;
import lombok.Getter;

@Getter
public enum ExerciseType {

    NONE("안해요"),
    DORMITORY("긱사에서"),
    GYM("헬스장에서");

    private final String description;

    ExerciseType(String description) {
        this.description = description;
    }

    public static ExerciseType fromDescription(String description) {
        if(description == null || description.isEmpty()) {
            return null;
        }

        for (ExerciseType type : ExerciseType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidExerciseTypeException();
    }
}
