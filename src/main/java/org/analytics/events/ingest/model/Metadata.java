package org.analytics.events.ingest.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Metadata(
    String version,
    String source,
    String environment
) {}
