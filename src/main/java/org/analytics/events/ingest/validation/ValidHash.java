package org.analytics.events.ingest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating hash strings.
 * Ensures that the annotated string value is a valid 32-character hexadecimal hash (MD5 format).
 */
@Documented
@Constraint(validatedBy = ValidHashValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHash {
  /**
   * Defines the error message when the hash validation fails.
   *
   * @return The error message.
   */
  String message() default "Invalid user hash format. Must be a 32-character hexadecimal string";

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
