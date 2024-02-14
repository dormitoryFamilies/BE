package dormitoryfamily.doomz.domain.article.entity.type;

import lombok.Getter;

@Getter
public enum StatusType {
    DONE("모집 완료"),
    PROGRESS("모집중");

    private String status;

    StatusType(String status) {
        this.status = status;
    }
}
