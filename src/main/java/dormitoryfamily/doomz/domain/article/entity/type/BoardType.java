package dormitoryfamily.doomz.domain.article.entity.type;

public enum BoardType {

    HELP("도와주세요"),
    TOGETHER("함께해요"),
    SHARE("나눔해요"),
    CURIOUS("궁금해요"),
    REPORT_LOST("분실신고");

    private final String description;

    BoardType(String description) {
        this.description = description;
    }
}
