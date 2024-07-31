package dormitoryfamily.doomz.domain.board.article.entity.type;

import dormitoryfamily.doomz.domain.board.article.exception.InvalidArticleDormitoryTypeException;
import lombok.Getter;

@Getter
public enum ArticleDormitoryType {

    MAIN_BUILDING("본관"),
    MALE_DORMITORY("양성재"),
    FEMALE_DORMITORY("양진재");

    private final String name;

    ArticleDormitoryType(String name) {
        this.name = name;
    }

    public static ArticleDormitoryType fromName(String name) {
        for (ArticleDormitoryType type : ArticleDormitoryType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new InvalidArticleDormitoryTypeException();
    }
}
