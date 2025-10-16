package org.analytics.events.ingest.model;

import jakarta.persistence.Embeddable;
import java.time.Instant;

@Embeddable
public record SessionInfo(
    String id,
    Instant startTime
) {}
