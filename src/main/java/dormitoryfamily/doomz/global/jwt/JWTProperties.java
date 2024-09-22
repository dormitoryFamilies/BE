package dormitoryfamily.doomz.global.jwt;

public class JWTProperties {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING_ACCESS = "AccessToken";
    public static final String HEADER_STRING_REFRESH = "RefreshToken";
    public static final String CATEGORY_ACCESS = "access";
    public static final String CATEGORY_REFRESH = "refresh";
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 60000 * 60 * 24 * 7; // 1주일  -> 추후 30분으로 변경
//    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 60000 * 60 * 24 * 14; // 2주일  -> 추후 7일로 변경
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 60000; // 1분으로 임시 수정
}
