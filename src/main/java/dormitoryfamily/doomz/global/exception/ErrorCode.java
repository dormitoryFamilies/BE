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
    ARTICLE_NOT_EXISTS(HttpStatus.NOT_FOUND, "해당 게시글은 존재하지 않습니다."),
    STATUS_ALREADY_SET(HttpStatus.BAD_REQUEST, null),

    // Member
    INVALID_MEMBER_ACCESS(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없는 사용자입니다."),
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    GENDER_TYPE_NOT_EXISTS(HttpStatus.BAD_REQUEST,"해당 성별은 존재하지 않습니다."),
    COLLEGE_TYPE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 단과대는 존재하지 않습니다."),
    DEPARTMENT_TYPE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 학과는 존재하지 않습니다."),
    COLLEGE_DEPARTMENT_MISMATCH(HttpStatus.BAD_REQUEST, "해당 단과대에는 해당 학과가 존재하지 않습니다."),
    INVALID_MEMBER_DORMITORY_TYPE(HttpStatus.BAD_REQUEST, "해당 멤버 기숙사는 존재하지 않습니다."),
    // comment
    COMMENT_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    COMMENT_IS_DELETED(HttpStatus.BAD_REQUEST, "삭제된 댓글입니다."),

    //replyComment
    REPLY_COMMENT_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 대댓글입니다."),

    //wish
    ALREADY_WISHED_ARTICLE(HttpStatus.CONFLICT, "이미 찜한 게시글입니다."),
    ARTICLE_IS_NOT_WISHED(HttpStatus.CONFLICT, "찜하지 않은 게시글입니다."),

    //follow
    ALREADY_FOLLOW_MEMBER(HttpStatus.CONFLICT, "이미 팔로우하고 있는 회원입니다."),
    CANNOT_FOLLOW_YOURSELF(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),
    MEMBER_IN_NOT_FOLLOWED(HttpStatus.CONFLICT, "팔로우하고 있지 않은 회원입니다."),

    //chatRoom
    CANNOT_CHAT_YOURSELF(HttpStatus.BAD_REQUEST, "자기 자신과 채팅할 수 없습니다."),
    CHAT_ROOM_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    ALREADY_CHAT_ROOM_LEFT(HttpStatus.CONFLICT, "이미 나간 채팅방입니다."),
    ALREADY_IN_CHAT_ROOM(HttpStatus.CONFLICT, "이미 채팅방에 입장한 상태입니다"),
    MEMBER_NOT_IN_CHAT_ROOM(HttpStatus.NOT_FOUND, "채팅방에 속해있지 않는 사용자입니다."),

    //chat
    CHAT_NOT_EXISTS(HttpStatus.BAD_REQUEST, "채팅 기록이 존재하지 않는 채팅방입니다."),
    CHAT_SINGLE_CONTENT_REQUIRED(HttpStatus.CONFLICT, "메시지 또는 이미지 URL 중 하나만 존재해야 합니다."),

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
