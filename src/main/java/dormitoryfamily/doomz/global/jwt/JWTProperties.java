package dormitoryfamily.doomz.global.jwt;

public class JWTProperties {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING_ACCESS = "AccessToken";
    public static final String HEADER_STRING_REFRESH = "RefreshToken";
    public static final String CATEGORY_ACCESS = "access";
    public static final String CATEGORY_REFRESH = "refresh";
    //    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 60000 * 10000; // 1분 * 10000 =
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 60000; // 1분 * 10000 =
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 60000 * 5; // 5분
}
