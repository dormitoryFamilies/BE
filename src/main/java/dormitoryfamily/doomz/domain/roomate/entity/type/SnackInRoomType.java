package dormitoryfamily.doomz.domain.roomate.entity.type;

import dormitoryfamily.doomz.domain.roomate.exception.InvalidSnackInRoomTypeException;
import lombok.Getter;

@Getter
public enum SnackInRoomType {

    ALLOWED("괜찮아요"),
    NOT_ALLOWED("싫어요");

    private final String description;

    SnackInRoomType(String description) {
        this.description = description;
    }

    public static SnackInRoomType fromDescription(String description) {
        for (SnackInRoomType type : SnackInRoomType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidSnackInRoomTypeException();
    }
}
