package org.analytics.events.ingest.model;

import jakarta.persistence.Embeddable;

/**
 * Represents metadata associated with an event, such as version, source, and environment.
 * This is an embeddable record suitable for JPA @Embedded annotation.
 */
@Embeddable
public record Metadata(
    String version,
    String source,
    String environment
) {}
