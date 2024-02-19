package dormitoryfamily.doomz.domain.article.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TagCountConstraintValidator.class)
public @interface TagCountConstraint {

    int value() default 2;
    String message() default "입력 가능한 태그 개수를 초과하였습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
