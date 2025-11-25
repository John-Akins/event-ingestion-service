package org.analytics.events.ingest.service;

import org.analytics.events.ingest.dto.EventRequest;
import org.analytics.events.ingest.mapper.EventMapper;
import org.analytics.events.ingest.model.BaseEvent;
import org.analytics.events.ingest.model.EventType;
import org.analytics.events.ingest.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTests {

    @Mock
    private EventRepository repository;

    @Mock
    private EventMapper mapper;

    @InjectMocks
    private EventService service;

    private EventRequest validEventRequest;
    private BaseEvent mappedEvent;

    @BeforeEach
    void setUp() {
        validEventRequest = new EventRequest(
                EventType.PAGE_VIEW,
                "test-hash",
                new EventRequest.ClientInformation("Mozilla", "127.0.0.1", "en-US", "UTC", "web"),
                Map.of("page", "/home")
        );

        mappedEvent = BaseEvent.builder()
                .eventType(EventType.PAGE_VIEW)
                .userHash("test-hash")
                .addData("page", "/home")
                .build();

        // Configure mapper to return mapped event for any EventRequestDTO
        lenient().when(mapper.toEvent(any(EventRequest.class))).thenReturn(mappedEvent);
    }

    @Test
    void shouldProcessSingleEvent() {
        // Given
        List<EventRequest> events = List.of(validEventRequest);

        // When
        service.processEvents(events);

        // Then
        verify(mapper, times(1)).toEvent(validEventRequest);
        verify(repository, times(1)).save(any(BaseEvent.class));
    }

    @Test
    void shouldProcessMultipleEvents() {
        // Given
        EventRequest event2 = new EventRequest(
                EventType.USER_ACTION,
                "hash2",
                null,
                Map.of("action", "click")
        );
        List<EventRequest> events = List.of(validEventRequest, event2);

        // When
        service.processEvents(events);

        // Then
        verify(mapper, times(2)).toEvent(any(EventRequest.class));
        verify(repository, times(2)).save(any(BaseEvent.class));
    }

    @Test
    void shouldHandleEmptyEventList() {
        // Given
        List<EventRequest> events = List.of();

        // When
        service.processEvents(events);

        // Then
        verify(mapper, never()).toEvent(any());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldHandleEventWithComplexData() {
        // Given
        EventRequest complexEvent = new EventRequest(
                EventType.ERROR,
                "error-hash",
                new EventRequest.ClientInformation("Chrome", "192.168.1.1", "de-DE", "CET", "desktop"),
                Map.of(
                        "errorMessage", "NullPointerException",
                        "stackTrace", "at com.example.Service.doSomething(Service.java:42)",
                        "userAgent", "Mozilla/5.0...",
                        "timestamp", System.currentTimeMillis()
                )
        );
        List<EventRequest> events = List.of(complexEvent);

        // When
        service.processEvents(events);

        // Then
        verify(mapper, times(1)).toEvent(complexEvent);
        verify(repository, times(1)).save(any(BaseEvent.class));
    }

    @Test
    void shouldHandleNullUserHash() {
        // Given
        EventRequest eventWithNullHash = new EventRequest(
                EventType.API_CALL,
                null,
                null,
                Map.of("endpoint", "/api/users")
        );
        List<EventRequest> events = List.of(eventWithNullHash);

        // When
        service.processEvents(events);

        // Then
        verify(mapper, times(1)).toEvent(eventWithNullHash);
        verify(repository, times(1)).save(any(BaseEvent.class));
    }

    @Test
    void shouldHandleAllEventTypes() {
        // Given - Use valid hash format that passes @ValidHash validation
        String validHash = "e9c0494b2b14ca2b48258c05dd6c4c14";
        List<EventRequest> eventsWithAllTypes = List.of(
                new EventRequest(EventType.PAGE_VIEW, validHash, null, Map.of()),
                new EventRequest(EventType.USER_ACTION, validHash, null, Map.of()),
                new EventRequest(EventType.ERROR, validHash, null, Map.of()),
                new EventRequest(EventType.FORM_SUBMIT, validHash, null, Map.of()),
                new EventRequest(EventType.API_CALL, validHash, null, Map.of()),
                new EventRequest(EventType.PERFORMANCE, validHash, null, Map.of()),
                new EventRequest(EventType.FEATURE_USAGE, validHash, null, Map.of()),
                new EventRequest(EventType.USER_PREFERENCE, validHash, null, Map.of()),
                new EventRequest(EventType.SEARCH, validHash, null, Map.of()),
                new EventRequest(EventType.AUTHENTICATION, validHash, null, Map.of())
        );

        // When
        service.processEvents(eventsWithAllTypes);

        // Then
        verify(mapper, times(10)).toEvent(any(EventRequest.class));
        verify(repository, times(10)).save(any(BaseEvent.class));
    }
}
