package org.analytics.events.ingest.repository;

import java.util.UUID;
import org.analytics.events.ingest.model.BaseEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link BaseEvent} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations.
 */
@Repository
public interface EventRepository extends JpaRepository<BaseEvent, UUID> {
}
