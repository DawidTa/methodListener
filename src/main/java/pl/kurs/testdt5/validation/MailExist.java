package pl.kurs.testdt5.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MailExistValidator.class)
public @interface MailExist {
    String message() default "Mail already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
