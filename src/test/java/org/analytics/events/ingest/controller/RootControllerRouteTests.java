package org.analytics.events.ingest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RootController.class)
@DisplayName("Root Controller API")
public class RootControllerRouteTests {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Given a request to the root endpoint")
    class RootEndpointTests {

        @Test
        @DisplayName("""
                When GET / is called,
                Then return welcome message with 200 OK""")
        void shouldReturnWelcomeMessage() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Welcome to Event ingestion service"));
        }
    }

    @Nested
    @DisplayName("Given a request to the API version endpoint")
    class ApiVersionEndpointTests {

        @Test
        @DisplayName("""
                When GET /api/v1 is called,
                Then return version message with 200 OK""")
        void shouldReturnVersionMessage() throws Exception {
            mockMvc.perform(get("/api/v1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Event ingestion Service version 1"));
        }
    }
}
