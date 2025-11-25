package org.analytics.events.ingest.model;

import jakarta.persistence.Embeddable;
import java.time.Instant;

/**
 * Represents session information, including a session ID and start time.
 * This is an embeddable record suitable for JPA @Embedded annotation.
 */
@Embeddable
public record SessionInfo(
    String id,
    Instant startTime
) {}
