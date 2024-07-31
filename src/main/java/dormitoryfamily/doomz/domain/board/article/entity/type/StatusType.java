package dormitoryfamily.doomz.domain.board.article.entity.type;

import dormitoryfamily.doomz.domain.board.article.exception.InvalidStatusTypeException;
import lombok.Getter;

@Getter
public enum StatusType {

    DONE("모집완료"),
    PROGRESS("모집중");

    private final String description;

    StatusType(String description) {
        this.description = description;
    }

    public static StatusType fromDescription(String description) {
        for (StatusType type : StatusType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidStatusTypeException();
    }
}
