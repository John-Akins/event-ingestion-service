package org.analytics.events.ingest.model;

public record ClientInfo(
    String userAgent,
    String ipAddress,
    String locale,
    String timezone,
    String platform
) {}
