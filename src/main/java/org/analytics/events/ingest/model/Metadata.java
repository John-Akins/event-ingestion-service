package org.analytics.events.ingest.model;

public record Metadata(
    String version,
    String source,
    String environment
) {}
