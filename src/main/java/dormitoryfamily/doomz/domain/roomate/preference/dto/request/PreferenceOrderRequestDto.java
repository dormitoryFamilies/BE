package dormitoryfamily.doomz.domain.roomate.preference.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PreferenceOrderRequestDto(

        @NotBlank(message = "필수 값 입니다.")
        String firstPreference,

        @NotBlank(message = "필수 값 입니다.")
        String secondPreference,

        @NotBlank(message = "필수 값 입니다.")
        String thirdPreference,

        @NotBlank(message = "필수 값 입니다.")
        String fourthPreference
) {
}
