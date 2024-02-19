package dormitoryfamily.doomz.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // S3
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "첨부 파일이 없습니다."),
    FILE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "삭제할 파일이 저장 공간에 존재하지 않습니다."),
    MAX_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "허용 용량을 초과한 파일입니다."),

    // Article
    ARTICLE_DORMITORY_TYPE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 기숙사 유형이 존재하지 않습니다."),
    BOARD_TYPE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 게시판 유형이 존재하지 않습니다."),
    STATUS_TYPE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 게시글 상태는 존재하지 않습니다"),
    ARTICLE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 게시글은 존재하지 않습니다."),
    STATUS_ALREADY_SET(HttpStatus.BAD_REQUEST, null),

    // Member
    INVALID_MEMBER_ACCESS(HttpStatus.NOT_FOUND, "해당 게시글에 대한 권한이 없습니다."),

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
