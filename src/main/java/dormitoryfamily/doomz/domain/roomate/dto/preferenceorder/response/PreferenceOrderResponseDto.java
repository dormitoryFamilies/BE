package dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.response;

import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.type.Describable;

public record PreferenceOrderResponseDto(

        String firstPreferenceOrder,

        String secondPreferenceOrder,

        String thirdPreferenceOrder,

        String fourthPreferenceOrder

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
        if (preference instanceof Describable describable) {
            return preference.getClass().getSimpleName() + ":" + describable.getDescription();
        }
        return preference.name();
    }
}
