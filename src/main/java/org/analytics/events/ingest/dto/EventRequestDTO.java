package org.analytics.events.ingest.dto;

import java.util.Map;

import org.analytics.events.ingest.model.EventType;
import org.analytics.events.ingest.validation.ValidHash;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


public record EventRequestDTO(
    @NotNull(message = "Event type is required")
    EventType eventType,
    
    @NotBlank(message = "User hash is required")
    @ValidHash
    String userHash,
    
    ClientInfoDTO clientInfo,
    
    @NotNull(message = "Event data is required")
    Map<String, Object> data
) {
    public record ClientInfoDTO(
        String userAgent,
        String ipAddress,
        String locale,
        String timezone,
        String platform
    ) {}
}
