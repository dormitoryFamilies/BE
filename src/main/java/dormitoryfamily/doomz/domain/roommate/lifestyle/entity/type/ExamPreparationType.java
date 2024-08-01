package dormitoryfamily.doomz.domain.roommate.lifestyle.entity.type;

import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.InvalidExamPreparationTypeException;
import lombok.Getter;

@Getter
public enum ExamPreparationType implements LifestyleAttribute {

    PREPARING("시험 준비", 0),
    NONE("해당 없어요", 8);

    private final String description;
    private final int index;

    ExamPreparationType(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static ExamPreparationType fromDescription(String description) {
        for (ExamPreparationType type : ExamPreparationType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidExamPreparationTypeException();
    }
}
