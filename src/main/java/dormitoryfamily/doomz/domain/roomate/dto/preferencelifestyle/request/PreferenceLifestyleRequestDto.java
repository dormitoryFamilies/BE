package dormitoryfamily.doomz.domain.roomate.dto.preferencelifestyle.request;

import jakarta.validation.constraints.NotBlank;

public record PreferenceLifestyleRequestDto(

        @NotBlank(message = "필수 값 입니다.")
        String firstPreferenceType,
        @NotBlank(message = "필수 값 입니다.")
        String firstPreference,

        @NotBlank(message = "필수 값 입니다.")
        String secondPreferenceType,
        @NotBlank(message = "필수 값 입니다.")
        String secondPreference,

        @NotBlank(message = "필수 값 입니다.")
        String thirdPreferenceType,
        @NotBlank(message = "필수 값 입니다.")
        String thirdPreference,

        @NotBlank(message = "필수 값 입니다.")
        String fourthPreferenceType,
        @NotBlank(message = "필수 값 입니다.")
        String fourthPreference

) {
}
