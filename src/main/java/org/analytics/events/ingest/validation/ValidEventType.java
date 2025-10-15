package org.analytics.events.ingest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ValidEventTypeValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventType {
    String message() default "Invalid event type provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
