package dormitoryfamily.doomz.global.chat;

public class ChatProperties {

    public static final String CONSUMER_GROUP = "chat-consumer-group";
    public static final String CONSUMER_NAME = "chat-consumer";
    public static final String STREAM_KEY_PREFIX = "chat:stream:";
    public static final String CACHE_KEY_PREFIX = "chat:cache:";
    public static final int STREAM_POOL_SIZE = 10; // 스트림 풀 크기

    public static String getStreamKey(String roomUUID) {
        int poolIndex = getStreamPoolIndex(roomUUID);
        return STREAM_KEY_PREFIX + poolIndex;
    }

    public static String getCacheKey(String roomUUID) {
        return CACHE_KEY_PREFIX + roomUUID;
    }

    private static int getStreamPoolIndex(String roomUUID) {
        return Math.abs(roomUUID.hashCode() % STREAM_POOL_SIZE);
    }
}
