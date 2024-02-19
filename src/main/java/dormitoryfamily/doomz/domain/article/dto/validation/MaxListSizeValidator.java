package dormitoryfamily.doomz.domain.article.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class MaxListSizeValidator implements ConstraintValidator<MaxListSize, List<String>> {

    private int maxSize;

    @Override
    public void initialize(MaxListSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(List<String> imagesUrls, ConstraintValidatorContext constraintValidatorContext) {
        return imagesUrls.size() <= maxSize;
    }
}
