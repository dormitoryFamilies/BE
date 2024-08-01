package dormitoryfamily.doomz.domain.roomate.preference.dto.request;

import dormitoryfamily.doomz.domain.roomate.preference.dto.validation.ValidPreferenceType;
import jakarta.validation.constraints.NotBlank;

public record PreferenceOrderRequestDto(

        @NotBlank(message = "필수 값 입니다.")
        @ValidPreferenceType
        String firstPreference,

        @NotBlank(message = "필수 값 입니다.")
        @ValidPreferenceType
        String secondPreference,

        @NotBlank(message = "필수 값 입니다.")
        @ValidPreferenceType
        String thirdPreference,

        @NotBlank(message = "필수 값 입니다.")
        @ValidPreferenceType
        String fourthPreference
) {
}
