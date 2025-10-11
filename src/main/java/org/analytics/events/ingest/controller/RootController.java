package org.analytics.events.ingest.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RootController handles the root and API version endpoints for the Event Ingestion Service.
 * <p>
 * Provides basic information about the service and its version.
 * </p>
 *
 * Endpoints:
 * <ul>
 *   <li><b>GET /</b>: Returns a welcome message for the Event Ingestion Service.</li>
 *   <li><b>GET /api/v1</b>: Returns a message indicating the version of the Event Ingestion Service API.</li>
 * </ul>
 */
@RestController
public class RootController {
    @GetMapping("/")
    public String root() {
        return new String("Welcome to Event ingestion service");
    }

    @GetMapping("/api/v1")
    public Map<String, String> getAPIVersion1Message() {
        return Collections.singletonMap("message", "Event ingestion Service version 1");
    }
}
