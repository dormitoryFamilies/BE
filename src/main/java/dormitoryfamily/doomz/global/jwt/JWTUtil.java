package dormitoryfamily.doomz.global.jwt;

import dormitoryfamily.doomz.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtHandler;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    public String createToken(Member member) {
        Claims subject = (Claims) Jwts.claims().subject(member.getEmail());
        Pair<String, Key> key = JwtKey.getRandomKey();

        return Jwts.builder()
                .claim("email", member.getEmail())
                .claim("role", member.getAuthority())
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발생 시간
                .expiration(new Date(System.currentTimeMillis() + JWTProperties.ACCESS_TOKEN_EXPIRATION_TIME)) // 토큰 만료 시간
                .header()
                .keyId(key.getFirst()) //kid 설정
                .and()
                .signWith(key.getSecond()) // signature
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser().build().parseSignedClaims(token).getPayload().get("email", String.class);
    }
}
