package dormitoryfamily.doomz.global.security.dto;

import dormitoryfamily.doomz.domain.member.entity.type.GenderType;

import java.time.LocalDate;
import java.util.Map;

public class KakaoMemberResponse {

    private final Map<String, Object> attributes;

    public KakaoMemberResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");
        return properties.get("name").toString();
    }

    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount.get("email").toString();
    }

    public String getProfileImage() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties.get("profile_image").toString();
    }

    public LocalDate getBirthDate() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        int birthYear = Integer.parseInt(kakaoAccount.get("birthyear").toString());
        String birthDate = kakaoAccount.get("birthday").toString();
        int birthMonth = Integer.parseInt(birthDate.substring(0, 2));
        int birthDay = Integer.parseInt(birthDate.substring(2));

        return LocalDate.of(birthYear, birthMonth, birthDay);
    }

    public GenderType getGender() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String gender = kakaoAccount.get("gender").toString();
        if (gender.equals("male")) {
            return GenderType.MALE;
        }
        return GenderType.FEMALE;
    }
}
