package org.analytics.events.ingest.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * A custom health indicator for the event ingestion service.
 * <p>
 * This component provides a custom health check that integrates with Spring
 * Boot Actuator. It can be
 * extended to include checks for database connectivity, external service
 * availability, or other
 * critical components.
 * </p>
 */
@Component
public class HealthCheck implements HealthIndicator {

  // ADD custom healthcheck to event ingestion service

  @Override
  public Health health() {
    // TODO: Implement custom health check with indicator of why service is down
    return Health.up().withDetail("Custom Health Check Status", "OK!").build();
  }
}
