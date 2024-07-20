package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidExamPreparationTypeException;
import lombok.Getter;

@Getter
public enum ExamPreparationType implements Describable {

    PREPARING("시험 준비"),
    NONE("해당 없어요");

    private final String description;

    ExamPreparationType(String description) {
        this.description = description;
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
