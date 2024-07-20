package dormitoryfamily.doomz.domain.roomate.dto.preferenceorder.response;

import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import dormitoryfamily.doomz.domain.roomate.entity.type.Describable;

import java.util.List;

public record PreferenceOrderResponseDto(

        String firstPreferenceType,
        String firstPreference,

        String secondPreferenceType,
        String secondPreference,

        String thirdPreferenceType,
        String thirdPreference,

        String fourthPreferenceType,
        String fourthPreference

) {
    public static PreferenceOrderResponseDto fromEntity(List<PreferenceOrder> preferenceOrders) {
        String firstPreferenceType = null;
        String firstPreference = null;
        String secondPreferenceType = null;
        String secondPreference = null;
        String thirdPreferenceType = null;
        String thirdPreference = null;
        String fourthPreferenceType = null;
        String fourthPreference = null;

        for (PreferenceOrder preferenceOrder : preferenceOrders) {
            String preferenceTypeDetail = ((Describable) preferenceOrder.getLifestyleDetail()).getDescription();

            switch (preferenceOrder.getPreferenceOrder()) {
                case 1 -> {
                    firstPreferenceType = preferenceOrder.getLifestyleType().name();
                    firstPreference = preferenceTypeDetail;
                }
                case 2 -> {
                    secondPreferenceType = preferenceOrder.getLifestyleType().name();
                    secondPreference = preferenceTypeDetail;
                }
                case 3 -> {
                    thirdPreferenceType = preferenceOrder.getLifestyleType().name();
                    thirdPreference = preferenceTypeDetail;
                }
                case 4 -> {
                    fourthPreferenceType = preferenceOrder.getLifestyleType().name();
                    fourthPreference = preferenceTypeDetail;
                }
            }
        }

        return new PreferenceOrderResponseDto(
                firstPreferenceType, firstPreference,
                secondPreferenceType, secondPreference,
                thirdPreferenceType, thirdPreference,
                fourthPreferenceType, fourthPreference
        );
    }
}
