package org.analytics.events.ingest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Event Ingestion Service.
 * This class uses Spring Boot to autoconfigure and run the application.
 */
@SpringBootApplication
public class EventIngestionService {

  public static void main(String[] args) {
    SpringApplication.run(EventIngestionService.class, args);
  }

}
