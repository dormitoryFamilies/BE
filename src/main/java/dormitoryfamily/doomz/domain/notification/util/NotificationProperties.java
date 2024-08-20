package dormitoryfamily.doomz.domain.notification.util;

public class NotificationProperties {

    public static final String SEPARATOR = "_";
    public static final String EVENT_NAME = "sse";
    public static final long DEFAULT_TIMEOUT = 3600L * 1000; //60분
    public static final int DELETE_AFTER_DAYS = 30; //알림 삭제 기한 (단위: 일)
    public static final String NEW_NOTIFICATION_CREATED = "new notification created";
}
