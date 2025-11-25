package org.analytics.events.ingest.mapper;

import org.analytics.events.ingest.dto.EventRequest;
import org.analytics.events.ingest.dto.EventRequest.ClientInformation;
import org.analytics.events.ingest.model.BaseEvent;
import org.analytics.events.ingest.model.ClientInfo;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for converting {@link EventRequest} DTOs to {@link BaseEvent} entities.
 * It handles transformation logic between API request format and the internal event model.
 */
@Component
public class EventMapper {

  /**
   * Converts an {@link EventRequest} DTO to a {@link BaseEvent} entity.
   *
   * @param dto The {@link EventRequest} DTO to convert.
   * @return A new {@link BaseEvent} instance populated with data from the DTO.
   */
  public BaseEvent toEvent(EventRequest dto) {
    return BaseEvent.builder().eventType(dto.eventType()).userHash(dto.userHash())
        .clientInfo(mapClientInfo(dto.clientInfo())).data(dto.data()).build();
  }

  /**
   * Maps a {@link ClientInformation} DTO to a {@link ClientInfo} model.
   *
   * @param dto The client information DTO.
   * @return A {@link ClientInfo} object, or {@code null} if the input DTO is {@code null}.
   */
  private ClientInfo mapClientInfo(ClientInformation dto) {
    if (dto == null) {
      return null;
    }
    return new ClientInfo(dto.userAgent(), dto.ipAddress(), dto.locale(), dto.timezone(),
        dto.platform());
  }
}
