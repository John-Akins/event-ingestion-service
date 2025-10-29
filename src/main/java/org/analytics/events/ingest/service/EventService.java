package org.analytics.events.ingest.service;

import java.util.List;

import org.analytics.events.ingest.dto.EventRequestDTO;
import org.analytics.events.ingest.mapper.EventMapper;
import org.analytics.events.ingest.model.BaseEvent;
import org.analytics.events.ingest.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private EventMapper mapper;

    public void processEvents(List<EventRequestDTO> events) {
        for (EventRequestDTO eventDTO : events) {
            BaseEvent event = mapper.toEvent(eventDTO);
            repository.save(event);
        }
    }
}
