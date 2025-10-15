package org.analytics.events.ingest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidHashValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHash {
    String message() default "Invalid user hash format. Must be a 32-character hexadecimal string";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
