package pl.methodListener.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginExistValidator.class)
public @interface LoginExist {
    String message() default "Login already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
