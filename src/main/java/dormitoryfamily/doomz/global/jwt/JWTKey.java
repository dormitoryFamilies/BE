package dormitoryfamily.doomz.global.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

public class JWTKey {

    private static Dotenv dotenv = Dotenv.load();
    private static final String JWT_SECRET_KEY1 = dotenv.get("JWT_SECRET_KEY1");
    private static final String JWT_SECRET_KEY2 = dotenv.get("JWT_SECRET_KEY2");
    private static final String JWT_SECRET_KEY3 = dotenv.get("JWT_SECRET_KEY3");
    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", JWT_SECRET_KEY1,
            "key2", JWT_SECRET_KEY2,
            "key3", JWT_SECRET_KEY3
    );
    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static Random randomIndex = new Random();

    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.get(kid);
        if (key == null) {
            return null;
        }
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
