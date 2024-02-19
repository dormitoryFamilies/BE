package dormitoryfamily.doomz.domain.article.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxListSizeValidator.class)
public @interface MaxListSize {

    int value() default 3;
    String message() default "첨부 가능한 이미지 개수를 초과하였습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
