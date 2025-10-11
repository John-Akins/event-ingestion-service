package org.analytics.events.ingest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link RootController} routes using Spring's MockMvc.
 * <p>
 * This test class verifies the following endpoints:
 * <ul>
 *   <li><code>GET /</code>: Ensures the root route returns a welcome message with HTTP 200 status.</li>
 *   <li><code>GET /api/v1</code>: Ensures the API version route returns a JSON message indicating the service version with HTTP 200 status.</li>
 * </ul>
 */
@WebMvcTest(RootController.class)
public class RootControllerRouteTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void rootRouteReturnsWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string("Welcome to Event ingestion service"));
    }

    @Test
    void apiV1RouteReturnsVersionMessage() throws Exception {
        mockMvc.perform(get("/api/v1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Event ingestion Service version 1"));
    }
}
