package org.analytics.events.ingest.dto;

import java.time.Instant;

/**
 * Represents a standard error response format for the API.
 * This record is used to provide consistent error messages to clients,
 * including details such as the HTTP status, error type, a descriptive message,
 * the request path, a timestamp, and a unique trace ID for logging and debugging.
 *
 * @param status    the HTTP status code of the error
 * @param error     a brief description of the error type
 * @param message   a more detailed message explaining the error
 * @param path      the URL path where the error occurred
 * @param timestamp the exact time the error was recorded
 * @param traceId   a unique identifier for tracing the request
 */
public record ErrorResponse(
    int status,
    String error,
    String message,
    String path,
    Instant timestamp,
    String traceId
) {

  /**
   * Constructs an {@code ErrorResponse} with the specified details,
   * automatically setting the timestamp to the current time.
   *
   * @param status  the HTTP status code
   * @param error   the error type
   * @param message the error message
   * @param path    the request path
   * @param traceId the trace ID
   */
  public ErrorResponse(int status, String error, String message, String path, String traceId) {
    this(status, error, message, path, Instant.now(), traceId);
  }

  /**
   * Constructs an {@code ErrorResponse} with the specified details,
   * setting the timestamp to the current time and the trace ID to {@code null}.
   *
   * @param status  the HTTP status code
   * @param error   the error type
   * @param message the error message
   * @param path    the request path
   */
  public ErrorResponse(int status, String error, String message, String path) {
    this(status, error, message, path, Instant.now(), null);
  }
}
