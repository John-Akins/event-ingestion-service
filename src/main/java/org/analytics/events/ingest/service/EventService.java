package org.analytics.events.ingest.service;

import java.util.List;
import org.analytics.events.ingest.dto.EventRequest;
import org.analytics.events.ingest.mapper.EventMapper;
import org.analytics.events.ingest.model.BaseEvent;
import org.analytics.events.ingest.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for processing and ingesting events.
 * Handles the business logic related to event processing,
 * including mapping event requests to event models and saving them to the repository.
 */
@Service
public class EventService {

  @Autowired
  private EventRepository repository;

  @Autowired
  private EventMapper mapper;

  /**
   * Processes a list of event requests.
   * Each event request is mapped to a {@link BaseEvent} and then saved to the repository.
   *
   * @param events A list of {@link EventRequest} objects to be processed.
   */
  public void processEvents(List<EventRequest> events) {
    for (EventRequest eventRequest : events) {
      BaseEvent event = mapper.toEvent(eventRequest);
      repository.save(event);
    }
  }
}
