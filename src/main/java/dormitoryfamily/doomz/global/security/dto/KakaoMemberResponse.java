package dormitoryfamily.doomz.global.security.dto;

import dormitoryfamily.doomz.domain.member.member.entity.type.GenderType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class KakaoMemberResponse {

    private final String name;
    private final String email;
    private final String profileImage;
    private final LocalDate birthDate;
    private final GenderType gender;

    public KakaoMemberResponse(JsonNode jsonNode) {
        JsonNode kakaoAccount = jsonNode.get("kakao_account");
        JsonNode properties = jsonNode.get("properties");

        this.name = kakaoAccount.has("name") ? kakaoAccount.get("name").asText() : null;
        this.email = kakaoAccount.has("email") ? kakaoAccount.get("email").asText() : null;
        this.profileImage = properties.has("profile_image") ? properties.get("profile_image").asText() : null;

        // Birthdate parsing
        if (kakaoAccount.has("birthyear") && kakaoAccount.has("birthday")) {
            int birthYear = Integer.parseInt(kakaoAccount.get("birthyear").asText());
            String birthDateString = kakaoAccount.get("birthday").asText();
            int birthMonth = Integer.parseInt(birthDateString.substring(0, 2));
            int birthDay = Integer.parseInt(birthDateString.substring(2));
            this.birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        } else {
            this.birthDate = null;
        }

        // Gender parsing
        if (kakaoAccount.has("gender")) {
            String genderString = kakaoAccount.get("gender").asText();
            if ("male".equals(genderString)) {
                this.gender = GenderType.MALE;
            } else if ("female".equals(genderString)) {
                this.gender = GenderType.FEMALE;
            } else {
                this.gender = null;
            }
        } else {
            this.gender = null;
        }
    }
}
