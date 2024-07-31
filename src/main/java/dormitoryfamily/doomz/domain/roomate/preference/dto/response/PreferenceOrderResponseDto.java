package dormitoryfamily.doomz.domain.roomate.preference.dto.response;

import dormitoryfamily.doomz.domain.roomate.preference.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.lifestyle.entity.type.LifestyleAttribute;

import static dormitoryfamily.doomz.domain.roomate.lifestyle.entity.type.LifestyleType.getRequiredEnumType;

public record PreferenceOrderResponseDto(

        String firstPreference,

        String secondPreference,

        String thirdPreference,

        String fourthPreference

) {
    public static PreferenceOrderResponseDto fromEntity(PreferenceOrder preference) {
        return new PreferenceOrderResponseDto(
                formatPreference(preference.getFirstPreferenceOrder()),
                formatPreference(preference.getSecondPreferenceOrder()),
                formatPreference(preference.getThirdPreferenceOrder()),
                formatPreference(preference.getFourthPreferenceOrder())
        );
    }

    private static String formatPreference(Enum<?> preference) {
        return getRequiredEnumType(preference.getClass().getSimpleName()) + ":" + ((LifestyleAttribute) preference).getDescription();
    }
}
