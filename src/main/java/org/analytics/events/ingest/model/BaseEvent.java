package org.analytics.events.ingest.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a base event for analytics ingestion with immutable data.
 * This class uses the Builder pattern to facilitate the creation of event instances
 * with optional parameters. The event is immutable once built.
 *
 * <p>Possible event types are defined in {@link EventType} enum and include:
 * PAGE_VIEW, USER_ACTION, ERROR, FORM_SUBMIT, API_CALL, PERFORMANCE,
 * FEATURE_USAGE, USER_PREFERENCE, SEARCH, AUTHENTICATION.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * BaseEvent event = BaseEvent.builder()
 *     .eventType("PAGE_VIEW")
 *     .userHash("e9c0494b2b14ca2b48258c05dd6c4c14")
 *     .clientInfo(new ClientInfo("Mozilla", "127.0.0.1", "en-US", "UTC", "web"))
 *     .addData("page", "/home")
 *     .addData("referrer", "/login")
 *     .build();
 * </pre>
 */
@Entity
@Table(name = "events")
public class BaseEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "user_hash")
    private String userHash;

    @Embedded
    private ClientInfo clientInfo;

    @Embedded
    private SessionInfo session;

    @Embedded
    private Metadata metadata;

    @Convert(converter = HashMapConverter.class)
    @Column(name = "data", columnDefinition = "jsonb") // Use jsonb for PostgreSQL
    private Map<String, Object> data = new HashMap<>();

    // Default constructor for JPA
    public BaseEvent() {
    }

    private BaseEvent(Builder builder) {
        this.eventId = builder.eventId;
        this.eventType = builder.eventType;
        this.timestamp = builder.timestamp;
        this.userHash = builder.userHash;
        this.clientInfo = builder.clientInfo;
        this.session = builder.session;
        this.metadata = builder.metadata;
        this.data = new HashMap<>(builder.data);
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters for JPA
    public UUID getEventId() { return eventId; }

    public EventType getEventType() { return eventType; }

    public Instant getTimestamp() { return timestamp; }

    public String getUserHash() { return userHash; }

    public ClientInfo getClientInfo() { return clientInfo; }

    public SessionInfo getSession() { return session; }

    public Metadata getMetadata() { return metadata; }

    public Map<String, Object> getData() { return new HashMap<>(data); }

    public static class Builder {
        private UUID eventId = UUID.randomUUID();
        private EventType eventType;
        private Instant timestamp = Instant.now();
        private String userHash;
        private ClientInfo clientInfo;
        private SessionInfo session;
        private Metadata metadata;
        private Map<String, Object> data = new HashMap<>();

        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder userHash(String userHash) {
            this.userHash = userHash;
            return this;
        }

        public Builder clientInfo(ClientInfo clientInfo) {
            this.clientInfo = clientInfo;
            return this;
        }

        public Builder session(SessionInfo session) {
            this.session = session;
            return this;
        }

        public Builder metadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder addData(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public Builder data(Map<String, Object> data) {
            this.data = new HashMap<>(data);
            return this;
        }

        public BaseEvent build() {
            return new BaseEvent(this);
        }
    }
}
