package dormitoryfamily.doomz.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.RECOMMENDATION_INTERVAL_HOURS;
import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    // S3
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_EMPTY(BAD_REQUEST, "첨부 파일이 없습니다."),
    FILE_NOT_EXISTS(BAD_REQUEST, "삭제할 파일이 저장 공간에 존재하지 않습니다."),
    MAX_SIZE_EXCEEDED(PAYLOAD_TOO_LARGE, "허용 용량을 초과한 파일입니다."),

    // Article
    ARTICLE_DORMITORY_TYPE_NOT_EXISTS(BAD_REQUEST, "해당 기숙사 유형이 존재하지 않습니다."),
    BOARD_TYPE_NOT_EXISTS(BAD_REQUEST, "해당 게시판 유형이 존재하지 않습니다."),
    STATUS_TYPE_NOT_EXISTS(BAD_REQUEST, "해당 게시글 상태는 존재하지 않습니다"),
    ARTICLE_NOT_EXISTS(NOT_FOUND, "해당 게시글은 존재하지 않습니다."),
    STATUS_ALREADY_SET(BAD_REQUEST, null),

    // Member
    INVALID_MEMBER_ACCESS(FORBIDDEN, "해당 요청에 대한 권한이 없는 사용자입니다."),
    MEMBER_NOT_EXISTS(NOT_FOUND, "존재하지 않는 회원입니다."),
    GENDER_TYPE_NOT_EXISTS(BAD_REQUEST,"해당 성별은 존재하지 않습니다."),
    COLLEGE_TYPE_NOT_EXISTS(BAD_REQUEST, "해당 단과대는 존재하지 않습니다."),
    DEPARTMENT_TYPE_NOT_EXISTS(BAD_REQUEST, "해당 학과는 존재하지 않습니다."),
    COLLEGE_DEPARTMENT_MISMATCH(BAD_REQUEST, "해당 단과대에는 해당 학과가 존재하지 않습니다."),
    MEMBER_DORMITORY_TYPE_NOT_EXISTS(BAD_REQUEST, "해당 멤버 기숙사 유형이 존재하지 않습니다."),
    NICKNAME_DUPLICATED(CONFLICT, "중복된 닉네임입니다."),
    NOT_INITIALIZE_PROFILE(UNAUTHORIZED, "초기 프로필 설정을 하지 않았습니다."),
    INVALID_MEMBER_DORMITORY_TYPE(BAD_REQUEST, "해당 멤버 기숙사는 존재하지 않습니다."),
    NOT_ROLE_VISITOR_OR_REJECTED_MEMBER(FORBIDDEN, "ROLE_VISITOR 또는 ROLE_REJECTED_MEMBER 권한만 요청할 수 있습니다."),
    NOT_ROLE_MEMBER(BAD_REQUEST, "대상의 권한이 ROLE_MEMBER 가 아닙니다."),

    // comment
    COMMENT_NOT_EXISTS(NOT_FOUND, "존재하지 않는 댓글입니다."),
    COMMENT_IS_DELETED(BAD_REQUEST, "삭제된 댓글입니다."),

    //replyComment
    REPLY_COMMENT_NOT_EXISTS(NOT_FOUND, "존재하지 않는 대댓글입니다."),

    //wish
    ALREADY_WISHED_ARTICLE(CONFLICT, "이미 찜한 게시글입니다."),
    ARTICLE_IS_NOT_WISHED(CONFLICT, "찜하지 않은 게시글입니다."),
    CANNOT_WISH_YOUR_ARTICLE(CONFLICT,"본인이 작성한 게시글은 찜할 수 없습니다."),

    //token
    REFRESH_TOKEN_NOT_EXISTS(BAD_REQUEST, "리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(BAD_REQUEST, "리프레시 토큰이 만료되었습니다."),
    INVALID_TOKEN_CATEGORY(BAD_REQUEST, "유효한 토큰 카테고리가 아닙니다."),
    NOT_SAVED_REFRESH_TOKEN(BAD_REQUEST, "해당 리프레시 토큰이 DB에 저장되어 있지 않습니다."),
    NOT_ACCESS_TOKEN(BAD_REQUEST, "엑세스 토큰이 아닙니다."),
    ACCESS_TOKEN_NOT_EXISTS(BAD_REQUEST, "엑세스 토큰이 존재하지 않습니다"),
    MEMBER_DATA_NOT_EXISTS_IN_DB(NOT_FOUND, "데이터 베이스에 해당 JWT 유저 정보가 존재하지 않습니다."),

    //follow
    ALREADY_FOLLOW_MEMBER(CONFLICT, "이미 팔로우하고 있는 회원입니다."),
    CANNOT_FOLLOW_YOURSELF(BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),
    MEMBER_IN_NOT_FOLLOWED(CONFLICT, "팔로우하고 있지 않은 회원입니다."),

    //chatRoom
    CANNOT_CHAT_YOURSELF(BAD_REQUEST, "자기 자신과 채팅할 수 없습니다."),
    CHAT_ROOM_NOT_EXISTS(NOT_FOUND, "존재하지 않는 채팅방입니다."),
    ALREADY_CHAT_ROOM_LEFT(CONFLICT, "이미 나간 채팅방입니다."),
    ALREADY_ENTERED_CHAT_ROOM(CONFLICT, "이미 채팅방에 입장한 상태입니다"),
    MEMBER_NOT_IN_CHAT_ROOM(NOT_FOUND, "채팅방에 속해있지 않는 사용자입니다."),
    CHAT_ROOM_NOT_EMPTY(BAD_REQUEST, "빈 채팅방이 아닙니다."),
    MEMBER_CHAT_ROOM_NOT_EXISTS(NOT_FOUND, "해당 사용자와의 채팅방이 존재하지 않습니다."),
    ALREADY_CHAT_ROOM_EXISTS(CONFLICT, "채팅방이 존재합니다. 재입장해주세요."),

    //chat
    CHAT_NOT_EXISTS(BAD_REQUEST, "채팅 기록이 존재하지 않는 채팅방입니다."),

    //recommendation
    RECOMMENDATION_NOT_EXISTS(NOT_FOUND, "룸메이트 매칭 추천을 요청한 적이 없습니다."),
    TOO_MANY_REQUEST(BAD_REQUEST, "매칭 추천 가능은 " + RECOMMENDATION_INTERVAL_HOURS + "시간 내 한 번만 가능합니다."),

    //lifestyle
    ALREADY_REGISTER_MY_LIFESTYLE(CONFLICT, "나의 라이프 스타일을 이미 설정했습니다."),
    LIFESTYLE_TYPE_NOT_EXISTS(BAD_REQUEST, "해당 생활 타입은 존재하지 않습니다."),
    SLEEPING_TIME_NOT_EXISTS(BAD_REQUEST, "해당 취침 시간은 존재하지 않습니다."),
    WAKE_UP_TIME_NOT_EXISTS(BAD_REQUEST, "해당 기상 시간은 존재하지 않습니다."),
    SLEEPING_HABIT_NOT_EXISTS(BAD_REQUEST, "해당 잠버릇은 존재하지 않습니다."),
    SLEEPING_SENSITIVITY_NOT_EXISTS(BAD_REQUEST, "해당 잠귀 타입은 존재하지 않습니다."),
    SMOKING_NOT_EXISTS(BAD_REQUEST, "해당 흡연 타입은 존재하지 않습니다."),
    DRINKING_FREQUENCY_NOT_EXISTS(BAD_REQUEST, "해당 음주 빈도는 존재하지 않습니다."),
    SHOWER_TIME_NOT_EXISTS(BAD_REQUEST, "해당 샤워 시간대는 존재하지 않습니다."),
    SHOWER_DURATION_NOT_EXISTS(BAD_REQUEST, "해당 샤워 시간은 존재하지 않습니다."),
    CLEANING_HABIT_NOT_EXISTS(BAD_REQUEST, "해당 청소 타입은 존재하지 않습니다."),
    HEAT_TOLERANCE_NOT_EXISTS(BAD_REQUEST, "해당 더위 타입은 존재하지 않습니다."),
    COLD_TOLERANCE_NOT_EXISTS(BAD_REQUEST, "해당 추위 타입은 존재하지 않습니다."),
    MBTI_NOT_EXISTS(BAD_REQUEST, "해당 MBTI 타입은 존재하지 않습니다."),
    VISIT_HOME_FREQUENCY_NOT_EXISTS(BAD_REQUEST, "해당 본가 가는 빈도는 존재하지 않습니다."),
    LATE_NIGHT_SNACK_NOT_EXISTS(BAD_REQUEST, "해당 야식 타입은 존재하지 않습니다."),
    SNACK_IN_ROOM_NOT_EXISTS(BAD_REQUEST, "해당 방안에서 타입은 존재하지 않습니다."),
    PHONE_SOUND_NOT_EXISTS(BAD_REQUEST, "해당 휴대폰 소리 타입은 존재하지 않습니다."),
    PERFUME_USAGE_NOT_EXISTS(BAD_REQUEST, "해당 향수 타입은 존재하지 않습니다."),
    STUDY_LOCATION_NOT_EXISTS(BAD_REQUEST, "해당 공부 장소는 존재하지 않습니다."),
    EXAM_PREPARATION_NOT_EXISTS(BAD_REQUEST, "해당 시험 타입은 존재하지 않습니다."),
    EXERCISE_NOT_EXISTS(BAD_REQUEST, "해당 운동 타입은 존재하지 않습니다."),
    INSECT_TOLERANCE_NOT_EXISTS(BAD_REQUEST, "해당 벌레 타입은 존재하지 않습니다."),
    LIFESTYLE_NOT_EXISTS(NOT_FOUND, "생활 타입을 아직 설정하지 않았습니다."),

    //preference
    ALREADY_REGISTER_PREFERENCE_ORDER(CONFLICT, "선호 우선순위를 이미 설정했습니다."),
    PREFERENCE_ORDER_NOT_EXISTS(NOT_FOUND, "선호 우선순위가 설정되어 있지 않습니다."),
    WRONG_PROPERTY(BAD_REQUEST, "해당 프로퍼티는 유효하지 않습니다."),
    DUPLICATE_PREFERENCE_ORDER_PARAMETER(BAD_REQUEST, "타입이 중복됩니다."),

    //matching request
    CANNOT_MATCHING_YOURSELF(CONFLICT, "자기 자신에게 매칭 신청을 할 수 없습니다."),
    ALREADY_MATCHING_REQUEST_EXISTS(CONFLICT, "이미 해당 사용자와 매칭 신청이 존재합니다."),
    MATCHING_REQUEST_NOT_EXISTS(NOT_FOUND, "해당 사용자와의 매칭 신청이 존재하지 않습니다."),
    ALREADY_MATCHED_MEMBER(CONFLICT, "이미 룸메이트 매칭이 완료된 사용자입니다."),
    MEMBER_DORMITORY_MISMATCH(CONFLICT, "기숙사가 달라 매칭이 불가능한 사용자입니다."),
    MATCHING_REQUEST_STATUS_NOT_EXISTS(NOT_FOUND, "해당 매칭 신청 타입은 존재하지 않습니다."),

    //matching result
    MATCHING_RESULT_NOT_EXISTS(CONFLICT,"해당 사용자와 룸메이트 매칭이 이루어지지 않은 상태입니다." ),

    //roommate wish
    ROOMMATE_WISH_NOT_EXIT(NOT_FOUND, "룸메이트 찜하지 않은 사용자입니다."),
    CANNOT_WISH_YOURSELF(CONFLICT, "자기 자신을 룸메이트 찜할 수 없습니다."),
    ALREADY_WISHED_ROOMMATE(CONFLICT, "이미 찜한 룸메이트 입니다."),

    //notification
    NOTIFICATION_NOT_EXISTS(NOT_FOUND, "해당 알림은 존재하지 않습니다."),
    ALREADY_READ_NOTIFICATION(CONFLICT, "해당 알림은 이미 읽음 처리가 되어 있습니다."),
    NOT_MY_NOTIFICATION(BAD_REQUEST, "본인 알림이 아닙니다."),
    JSON_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 에러가 발생했습니다."),
    MENU_FETCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"기숙사 식단 크롤링 url 연결에 실패했습니다." ),

    // 5xx
    INTERNAL_SERVER_ERROR(NOT_FOUND, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
