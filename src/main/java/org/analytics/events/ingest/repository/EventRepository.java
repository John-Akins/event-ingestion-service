package org.analytics.events.ingest.repository;

import org.analytics.events.ingest.model.BaseEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<BaseEvent, UUID> {
}
