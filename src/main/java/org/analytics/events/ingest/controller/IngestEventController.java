package org.analytics.events.ingest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.analytics.events.ingest.dto.EventRequest;
import org.analytics.events.ingest.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for ingesting analytics events.
 * Provides an endpoint for receiving and processing batches of event data.
 */
@RestController
@Validated
public class IngestEventController {

  @Autowired
  EventService eventService;
  /**
   * Ingests a batch of analytics events.
   *
   * <p>Accepting an array of events (batching) is a good practice
   * for analytics ingestion services due to:
   * <ul>
   * <li>Reduced Network Overhead: Fewer HTTP requests reduce service-level
   * network latency
   * at scale. However, this increases the request payload size, which can strain
   * network bandwidth and require servers to handle larger requests efficiently.
   * Individual event latency may also increase due to queuing delays.</li>
   * <li>Improved Throughput: Efficient processing of multiple events in
   * batches.</li>
   * <li>Batch Processing Efficiency: Leverages backend optimizations for batch
   * operations.</li>
   * <li>Resilience: Clients can queue events during network outages.</li>
   * </ul>
   * <p>
   * Batch size limit is around 100 events or a maximum payload size of 50KB.
   * This provides a good balance and should be fine-tuned based on actual load
   * testing.
   * </p>
   *
   * @param eventRequests A list of valid EventRequest objects.
   * @param request       The HttpServletRequest.
   * @return ResponseEntity indicating the success or failure of the ingestion.
   */

  @PostMapping("/ingest")
  public ResponseEntity<String> saveEvents(@RequestBody List<@Valid EventRequest> eventRequests,
      HttpServletRequest request) {
    if (eventRequests.isEmpty()) {
      return ResponseEntity.badRequest().body("Event list cannot be empty");
    }

    final long maxPayloadSize = 50 * 1024; // 50KB
    if (request.getContentLength() > maxPayloadSize) {
      return ResponseEntity.badRequest().body("Payload size cannot exceed 50KB");
    }

    if (eventRequests.size() > 100) {
      return ResponseEntity.badRequest().body("Batch size cannot exceed 100 events");
    }

    // TODO: Send events to event queue
    // to be processed by event service
    eventService.processEvents(eventRequests);

    return ResponseEntity.ok("Events ingested successfully: " + eventRequests.size());
  }
}
