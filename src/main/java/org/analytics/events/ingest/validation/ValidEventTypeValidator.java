package org.analytics.events.ingest.validation;

import org.analytics.events.ingest.model.EventType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEventTypeValidator implements ConstraintValidator<ValidEventType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are handled by @NotNull or @NotEmpty
        }
        try {
            EventType.fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
