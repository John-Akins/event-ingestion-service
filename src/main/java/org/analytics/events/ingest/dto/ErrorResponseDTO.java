package org.analytics.events.ingest.dto;

import java.time.Instant;

public record ErrorResponseDTO(
    int status,
    String error,
    String message,
    String path,
    Instant timestamp,
    String traceId
) {
    public ErrorResponseDTO(int status, String error, String message, String path, String traceId) {
        this(status, error, message, path, Instant.now(), traceId);
    }

    public ErrorResponseDTO(int status, String error, String message, String path) {
        this(status, error, message, path, Instant.now(), null);
    }
}
