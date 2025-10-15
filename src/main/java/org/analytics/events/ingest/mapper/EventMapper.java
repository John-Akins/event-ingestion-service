package org.analytics.events.ingest.mapper;

import org.analytics.events.ingest.dto.EventRequestDTO;
import org.analytics.events.ingest.model.BaseEvent;
import org.analytics.events.ingest.model.ClientInfo;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    
    public BaseEvent toEvent(EventRequestDTO dto) {
        return BaseEvent.builder()
            .eventType(dto.eventType())
            .userHash(dto.userHash())
            .clientInfo(mapClientInfo(dto.clientInfo()))
            .data(dto.data())
            .build();
    }
    
    private ClientInfo mapClientInfo(EventRequestDTO.ClientInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new ClientInfo(
            dto.userAgent(),
            dto.ipAddress(),
            dto.locale(),
            dto.timezone(),
            dto.platform()
        );
    }
}
