package dormitoryfamily.doomz.domain.article.entity.type;

import lombok.Getter;

@Getter
public enum StatusType {

    DONE("모집완료"),
    PROGRESS("모집중");

    private final String status;

    StatusType(String status) {
        this.status = status;
    }
}
