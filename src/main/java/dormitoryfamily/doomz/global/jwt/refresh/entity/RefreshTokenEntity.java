package dormitoryfamily.doomz.global.jwt.refresh.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "RefreshToken", timeToLive = 60 * 60 * 24 * 7L)
public class RefreshTokenEntity {

    @Id
    private String id;

    @Indexed
    private String email;

    private String refresh;

    public RefreshTokenEntity(String email, String refresh) {
        this.email = email;
        this.refresh = refresh;
    }
}
