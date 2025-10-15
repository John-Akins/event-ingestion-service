package org.analytics.events.ingest.model;

import java.time.Instant;

public record SessionInfo(
    String id,
    Instant startTime
) {}
