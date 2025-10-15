package org.analytics.events.ingest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.analytics.events.ingest.dto.EventRequestDTO;
import org.analytics.events.ingest.model.EventType;

@WebMvcTest(IngestEventController.class)
@DisplayName("App analytics ingestion requests")
public class IngestEventControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Nested
        @DisplayName("Given an empty request")
        class EmptyPayloadTests {

                @Test
                @DisplayName("""
                                When the request is sent with an empty array,
                                Then return 400 error""")
                void shouldReturn400ForEmptyArrayPayload() throws Exception {
                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("[]"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(content().string("Event list cannot be empty"));
                }
        }

        @Nested
        @DisplayName("Given a request with valid content")
        class ValidPayloadTests {

                private final ObjectMapper objectMapper = new ObjectMapper();

                private EventRequestDTO createValidEvent() {
                        return new EventRequestDTO(
                                        EventType.PAGE_VIEW,
                                        "e9c0494b2b14ca2b48258c05dd6c4c14",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                }

                @Test
                @DisplayName("""
                                Given a single valid event,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 200 OK response with a success message""")
                void shouldReturn200ForSingleValidEvent() throws Exception {
                        List<EventRequestDTO> events = Collections.singletonList(createValidEvent());
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isOk())
                                        .andExpect(content().string("Events ingested successfully: 1"));
                }

                @Test
                @DisplayName("""
                                Given multiple valid events,
                                When the events are sent to the ingest endpoint,
                                Then it should return a 200 OK response with a success message""")
                void shouldReturn200ForMultipleValidEvents() throws Exception {
                        List<EventRequestDTO> events = List.of(
                                        createValidEvent(),
                                        new EventRequestDTO(
                                                        EventType.USER_ACTION, // Changed to USER_ACTION as "CLICK" is
                                                                               // not a direct enum value
                                                        "a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6",
                                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                                        Map.of("element", "button_id")));
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isOk())
                                        .andExpect(content().string("Events ingested successfully: 2"));
                }
        }

        @Nested 
        @DisplayName("Given a request with wrong input")
        class WrongInputPayloadValidation {
                private final ObjectMapper objectMapper = new ObjectMapper();

                @Test
                @DisplayName("""
                                Given an event with an empty event type,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForEmptyEventTypeInEvent() throws Exception {
                        // This test case is now invalid as EventType is an enum and cannot be empty
                        // string.
                        // A @NotNull validation on EventType will catch this.
                        EventRequestDTO invalidEvent = new EventRequestDTO(
                                        null, // EventType cannot be an empty string, setting to null to trigger
                                              // @NotNull
                                        "e9c0494b2b14ca2b48258c05dd6c4c14",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                        List<EventRequestDTO> events = Collections.singletonList(invalidEvent);
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("""
                                Given an event with an empty user hash,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForEmptyUserHashInEvent() throws Exception {
                        EventRequestDTO invalidEvent = new EventRequestDTO(
                                        EventType.PAGE_VIEW,
                                        "",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                        List<EventRequestDTO> events = Collections.singletonList(invalidEvent);
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("""
                                Given an event with an invalid event type,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForInvalidEventTypeInEvent() throws Exception {
                        String payload = """
                                        [
                                            {
                                                "eventType": "INVALID_TYPE",
                                                "userHash": "e9c0494b2b14ca2b48258c05dd6c4c14",
                                                "data": {
                                                    "page": "/home"
                                                }
                                            }
                                        ]""";
                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("""
                                Given an event with an invalid user hash format,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForInvalidUserHashInEvent() throws Exception {
                        EventRequestDTO invalidEvent = new EventRequestDTO(
                                        EventType.PAGE_VIEW,
                                        "invalid-hash-format",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                        List<EventRequestDTO> events = Collections.singletonList(invalidEvent);
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

        }

        @Nested
        @DisplayName("Given a request with invalid Event content size")
        class PayloadContentSizeValidation {
                private final ObjectMapper objectMapper = new ObjectMapper();

                private EventRequestDTO createValidEvent() {
                        return new EventRequestDTO(
                                        EventType.PAGE_VIEW,
                                        "e9c0494b2b14ca2b48258c05dd6c4c14",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                }

                @Test
                @DisplayName("""
                                Given an event with an empty event type,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForEmptyEventTypeInEvent() throws Exception {
                        // This test case is now invalid as EventType is an enum and cannot be empty
                        // string.
                        // A @NotNull validation on EventType will catch this.
                        EventRequestDTO invalidEvent = new EventRequestDTO(
                                        null, // EventType cannot be an empty string, setting to null to trigger
                                              // @NotNull
                                        "e9c0494b2b14ca2b48258c05dd6c4c14",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                        List<EventRequestDTO> events = Collections.singletonList(invalidEvent);
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("""
                                Given an event with an empty user hash,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForEmptyUserHashInEvent() throws Exception {
                        EventRequestDTO invalidEvent = new EventRequestDTO(
                                        EventType.PAGE_VIEW,
                                        "",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                        List<EventRequestDTO> events = Collections.singletonList(invalidEvent);
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("""
                                Given an event with an invalid event type,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForInvalidEventTypeInEvent() throws Exception {
                        String payload = """
                                        [
                                            {
                                                "eventType": "INVALID_TYPE",
                                                "userHash": "e9c0494b2b14ca2b48258c05dd6c4c14",
                                                "data": {
                                                    "page": "/home"
                                                }
                                            }
                                        ]""";
                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("""
                                Given an event with an invalid user hash format,
                                When the event is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error""")
                void shouldReturn400ForInvalidUserHashInEvent() throws Exception {
                        EventRequestDTO invalidEvent = new EventRequestDTO(
                                        EventType.PAGE_VIEW,
                                        "invalid-hash-format",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        Map.of("page", "/home"));
                        List<EventRequestDTO> events = Collections.singletonList(invalidEvent);
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                @DisplayName("""
                                Given a batch of events exceeding 100,
                                When the batch is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error with the message 'Batch size cannot exceed 100 events'""")
                void shouldReturn400ForBatchSizeExceedingLimit() throws Exception {
                        List<EventRequestDTO> events = new java.util.ArrayList<>();
                        for (int i = 0; i <= 100; i++) { // 101 events
                                events.add(createValidEvent());
                        }
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(content().string("Batch size cannot exceed 100 events"));
                }
        }

        @Nested
        @DisplayName("Given request payload size exceeds limit")
        class PayloadSizeValidationTests {

                private final ObjectMapper objectMapper = new ObjectMapper();

                @Test
                @DisplayName("""
                                Given a request with a payload size exceeding 50KB,
                                When the request is sent to the ingest endpoint,
                                Then it should return a 400 Bad Request error with the message 'Payload size cannot exceed 50KB'""")
                void shouldReturn400ForPayloadSizeExceedingLimit() throws Exception {
                        // Create a large event to exceed 50KB payload size
                        // A single event with a large data map should be sufficient
                        Map<String, Object> largeData = new java.util.HashMap<>();
                        // Fill with ~50KB of data (1 character is 1 byte in UTF-8 for ASCII)
                        // 50KB = 50 * 1024 bytes. Let's aim for slightly over 50KB.
                        int targetSize = (50 * 1024) + 100; // Slightly over 50KB
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < targetSize / 10; i++) { // Divide by 10 for key/value pairs
                                sb.append("abcdefghij");
                        }
                        largeData.put("large_field", sb.toString());

                        EventRequestDTO largeEvent = new EventRequestDTO(
                                        EventType.PAGE_VIEW,
                                        "e9c0494b2b14ca2b48258c05dd6c4c14",
                                        new EventRequestDTO.ClientInfoDTO(null, null, null, null, null),
                                        largeData);
                        List<EventRequestDTO> events = Collections.singletonList(largeEvent);
                        String payload = objectMapper.writeValueAsString(events);

                        mockMvc.perform(post("/ingest")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(content().string("Payload size cannot exceed 50KB"));
                }
        }
}
