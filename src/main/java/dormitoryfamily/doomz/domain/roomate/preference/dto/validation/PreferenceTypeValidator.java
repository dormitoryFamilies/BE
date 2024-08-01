package dormitoryfamily.doomz.domain.roomate.preference.dto.validation;

import dormitoryfamily.doomz.domain.roomate.lifestyle.entity.type.LifestyleType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PreferenceTypeValidator implements ConstraintValidator<ValidPreferenceType, String> {

    @Override
    public boolean isValid(String preference, ConstraintValidatorContext context) {
        if (preference == null || preference.isEmpty()) {
            return true;
        }

        String preferenceType = preference.split(":")[0];
        LifestyleType lifestyleType = LifestyleType.fromType(preferenceType);
        return lifestyleType.isEssential();
    }
}
