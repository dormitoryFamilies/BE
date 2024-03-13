package dormitoryfamily.doomz.global.jwt;

import dormitoryfamily.doomz.domain.member.entity.Member;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    public String createToken(Member member) {
        Pair<String, Key> key = JWTKey.getRandomKey();

        return Jwts.builder()
                .subject(member.getEmail())
//                .claim("role", member.getAuthority())
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발생 시간
                .expiration(new Date(System.currentTimeMillis() + JWTProperties.ACCESS_TOKEN_EXPIRATION_TIME)) // 토큰 만료 시간
                .header()
                .keyId(key.getFirst()) //kid 설정
                .and()
                .signWith(key.getSecond()) // signature
                .compact();
    }

//    public Boolean isExpired(String token) {
//        return Jwts.parser()
//                .setSigningKeyResolver(SigningKeyResolver.instance)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getExpiration()
//                .before(new Date());
//    }

    public String getEmail(String token) {
        // jwtToken에서 email을 찾습니다.
        return Jwts.parser()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}