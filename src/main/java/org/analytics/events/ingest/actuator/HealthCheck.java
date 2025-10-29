package org.analytics.events.ingest.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator {		 
	// ADD custom healthcheck to event ingestion service

	@Override
	public Health health() {
		// TODO: Implement custom health check with indicator of why service is down

		return Health.up().build();
	}

}
