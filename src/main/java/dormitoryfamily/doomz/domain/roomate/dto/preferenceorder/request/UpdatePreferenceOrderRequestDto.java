package dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.request;

public record UpdatePreferenceOrderRequestDto(

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
