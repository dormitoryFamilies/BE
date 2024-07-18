package dormitoryfamily.doomz.domain.roomate.dto.preferencelifestyle.request;

public record PreferenceLifestyleRequestDto(
        String firstPreferenceType,
        String firstPreference,

        String secondPreferenceType,
        String secondPreference,

        String thirdPreferenceType,
        String thirdPreference,

        String fourthPreferenceType,
        String fourthPreference
) {
}
