package dormitoryfamily.doomz.domain.board.article.entity.type;

import dormitoryfamily.doomz.domain.board.article.exception.InvalidBoardTypeException;
import lombok.Getter;

@Getter
public enum BoardType {

    ALL("전체"),
    HELP("도와주세요"),
    TOGETHER("함께해요"),
    SHARE("나눔해요"),
    CURIOUS("궁금해요"),
    REPORT_LOST("분실신고");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }

    public static BoardType fromDescription(String description) {
        for (BoardType type : BoardType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new InvalidBoardTypeException();
    }
}
