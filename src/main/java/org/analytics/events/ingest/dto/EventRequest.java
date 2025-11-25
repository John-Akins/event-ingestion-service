package org.analytics.events.ingest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.analytics.events.ingest.model.EventType;
import org.analytics.events.ingest.validation.ValidHash;

/**
 * Represents the data transfer object for an incoming event request.
 * This record encapsulates all the necessary information for an event,
 * including its type, the user associated with it, client information,
 * and the event-specific data payload.
 *
 * @param eventType  the type of the event, which cannot be null
 * @param userHash   a unique hash identifying the user, which cannot be blank
 * @param clientInfo information about the client from which the event originated
 * @param data       a map containing the detailed event data, which cannot be null
 */
public record EventRequest(
    @NotNull(message = "Event type is required")
    EventType eventType,

    @NotBlank(message = "User hash is required")
    @ValidHash
    String userHash,

    ClientInformation clientInfo,

    @NotNull(message = "Event data is required")
    Map<String, Object> data
) {

  /**
   * Represents client-specific information included in an event request.
   * This record captures details about the client's environment, such as
   * the user agent, IP address, and locale.
   *
   * @param userAgent the user agent string of the client
   * @param ipAddress the IP address of the client
   * @param locale    the locale of the client
   * @param timezone  the timezone of the client
   * @param platform  the platform the client is running on
   */
  public record ClientInformation(
      String userAgent,
      String ipAddress,
      String locale,
      String timezone,
      String platform
  ) {

  }
}
