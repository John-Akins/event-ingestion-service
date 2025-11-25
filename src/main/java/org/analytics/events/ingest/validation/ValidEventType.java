package org.analytics.events.ingest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating event types.
 * Ensures that the annotated string value corresponds to a valid {@link EventType}.
 */
@Documented
@Constraint(validatedBy = ValidEventTypeValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventType {
  /**
   * Defines the error message when the event type validation fails.
   *
   * @return The error message.
   */
  String message() default "Invalid event type provided";

  /**
   * Defines the validation groups this constraint belongs to.
   *
   * @return An array of validation groups.
   */
  Class<?>[] groups() default {};

  /**
   * Defines the payload associated with this constraint.
   *
   * @return An array of payload classes.
   */
  Class<? extends Payload>[] payload() default {};
}
