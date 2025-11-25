package org.analytics.events.ingest.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * This class provides centralized exception handling for various types of exceptions
 * that may occur during request processing, returning appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation exceptions thrown when method arguments annotated with
   * {@code @Valid} fail validation.
   *
   * @param ex The {@link MethodArgumentNotValidException} that was thrown.
   * @return A {@link ResponseEntity} containing a map of field errors and their
   *         messages, with HTTP status 400 Bad Request.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach((error) -> {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles exceptions caused by constraint violations.
   *
   * @param ex The {@link ConstraintViolationException} that was thrown.
   * @return A {@link ResponseEntity} containing a map of constraint violation
   *         errors and their messages, with HTTP status 400 Bad Request.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String fieldName = violation.getPropertyPath().toString();
      String errorMessage = violation.getMessage();
      errors.put(fieldName, errorMessage);
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
