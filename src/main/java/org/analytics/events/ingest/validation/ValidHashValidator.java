package org.analytics.events.ingest.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidHashValidator implements ConstraintValidator<ValidHash, String> {

    // Pattern for 32-character hexadecimal string (MD5 hash format)
    private static final Pattern HASH_PATTERN = Pattern.compile("^[a-fA-F0-9]{32}$");

    @Override
    public boolean isValid(String userHash, ConstraintValidatorContext context) {
        if (userHash == null || userHash.trim().isEmpty()) {
            return false;
        }

        return HASH_PATTERN.matcher(userHash).matches();
    }
}
