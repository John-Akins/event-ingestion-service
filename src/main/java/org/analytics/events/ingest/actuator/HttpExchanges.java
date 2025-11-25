package org.analytics.events.ingest.actuator;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for exposing HTTP exchange traces.
 * <p>
 * This class provides a bean definition for {@link HttpExchangeRepository},
 * which enables the
 * {@code /httpexchanges} actuator endpoint. This is useful for monitoring and
 * debugging HTTP
 * requests and responses.
 * </p>
 */
@Configuration
public class HttpExchanges {

  @Bean
  public HttpExchangeRepository httpTraceRepository() {
    return new InMemoryHttpExchangeRepository();
  }
}
