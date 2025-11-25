package org.analytics.events.ingest.model;

import jakarta.persistence.Embeddable;

/**
 * Represents client information for an event (user agent, IP address, and platform).
 * This is an embeddable record suitable for JPA @Embedded annotation.
 */
@Embeddable
public record ClientInfo(
    String userAgent,
    String ipAddress,
    String locale,
    String timezone,
    String platform
) {
}
