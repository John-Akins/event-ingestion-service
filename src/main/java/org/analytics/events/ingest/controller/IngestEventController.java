package org.analytics.events.ingest.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.analytics.events.ingest.dto.EventRequestDTO;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@Validated
public class IngestEventController {

    /**
     * Ingests a batch of analytics events.
     *
     * As a System Reliability Lead Engineer, accepting an array of events (batching) is a good practice
     * for analytics ingestion services due to:
     * - Reduced Network Overhead: Fewer HTTP requests, lower latency.
     * - Improved Throughput: Efficient processing of multiple events.
     * - Batch Processing Efficiency: Leverages backend optimizations for batch operations.
     * - Resilience: Clients can queue events during network outages.
     *
     * Batch size limit is around 100 events or a maximum payload size of 50KB.
     * This provides a good balance and should be fine-tuned based on actual load testing.
     *
     * @param eventRequests A list of valid EventRequestDTO objects.
     * @param request The HttpServletRequest.
     * @return ResponseEntity indicating the success or failure of the ingestion.
     */
    @PostMapping("/ingest")
    public ResponseEntity<String> saveEvents(@RequestBody List<@Valid EventRequestDTO> eventRequests,
             HttpServletRequest request) {
        if (eventRequests.isEmpty()) {
            return ResponseEntity.badRequest().body("Event list cannot be empty");
        }

        final long MAX_PAYLOAD_SIZE = 50 * 1024; // 50KB
        if (request.getContentLength() > MAX_PAYLOAD_SIZE) {
            return ResponseEntity.badRequest().body("Payload size cannot exceed 50KB");
        }
        
        if (eventRequests.size() > 100) {
            return ResponseEntity.badRequest().body("Batch size cannot exceed 100 events");
        }

        // Simulate processing each event
        for (EventRequestDTO eventRequest : eventRequests) {
            // Here you would typically call a service to process each event
            System.out.println("Processing event: " + eventRequest.eventType() + " for user: " + eventRequest.userHash());
            // Example: eventService.processEvent(eventRequest, request);
        }

        return ResponseEntity.ok("Events ingested successfully: " + eventRequests.size());
    }
}
