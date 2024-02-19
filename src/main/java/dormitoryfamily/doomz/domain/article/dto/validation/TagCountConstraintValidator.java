package dormitoryfamily.doomz.domain.article.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TagCountConstraintValidator implements ConstraintValidator<TagCountConstraint, String> {

    private int maxSize;

    @Override
    public void initialize(TagCountConstraint constraintAnnotation) {
        this.maxSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String tags, ConstraintValidatorContext context) {
        if (tags == null || tags.isEmpty()) {
            return true;
        }

        String[] tagArray = tags.split("#");
        int tagCount = 0;
        for (String tag : tagArray) {
            if (!tag.trim().isEmpty()) {
                tagCount++;
            }
        }

        return tagCount <= maxSize;
    }
}
