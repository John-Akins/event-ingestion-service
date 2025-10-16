package org.analytics.events.ingest.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record ClientInfo(
    String userAgent,
    String ipAddress,
    String locale,
    String timezone,
    String platform
) {}
