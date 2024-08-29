package dormitoryfamily.doomz.global.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.jwt.JWTUtil;
import dormitoryfamily.doomz.global.jwt.refresh.entity.RefreshTokenEntity;
import dormitoryfamily.doomz.global.jwt.refresh.repository.RefreshTokenRepository;
import dormitoryfamily.doomz.global.oauth2.dto.JwtResponseDto;
import dormitoryfamily.doomz.global.security.dto.KakaoMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.*;

@Service
@RequiredArgsConstructor
public class UserKakaoService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUrl;

    public JwtResponseDto getJWTAccessToken(String accessToken) {
        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoUserInfoUrl,
                    HttpMethod.GET,
                    kakaoUserInfoRequest,
                    String.class
            );

            return processKakaoResponse(response.getBody());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset-utf-8");
        return headers;
    }

    private JwtResponseDto processKakaoResponse(String responseBody) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        KakaoMemberResponse kakaoMemberResponse = new KakaoMemberResponse(jsonNode);
        Member member = memberRepository.findByEmail(kakaoMemberResponse.getEmail())
                .orElseGet(() -> createNewMember(kakaoMemberResponse));

        String jwtAccessToken = jwtUtil.createToken(CATEGORY_ACCESS, member, ACCESS_TOKEN_EXPIRATION_TIME);
        String jwtRefreshToken = jwtUtil.createToken(CATEGORY_REFRESH, member, REFRESH_TOKEN_EXPIRATION_TIME);

        saveNewRefreshToken(member.getEmail(), jwtRefreshToken);

        return new JwtResponseDto(jwtAccessToken, jwtRefreshToken);
    }

    private Member createNewMember(KakaoMemberResponse kakaoMemberResponse) {
        Member newMember = Member.builder()
                .name(kakaoMemberResponse.getName())
                .email(kakaoMemberResponse.getEmail())
                .birthDate(kakaoMemberResponse.getBirthDate())
                .profileUrl(kakaoMemberResponse.getProfileImage())
                .genderType(kakaoMemberResponse.getGender())
                .build();

        return memberRepository.save(newMember);
    }

    private void saveNewRefreshToken(String email, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(email, refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }
}
