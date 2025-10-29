-- Create the events table for storing analytics event data
CREATE TABLE events (
    id UUID PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    event_timestamp TIMESTAMP NOT NULL,
    user_hash VARCHAR(255),
    user_agent VARCHAR(500),
    ip_address VARCHAR(45), -- Support for IPv6
    locale VARCHAR(10),
    timezone VARCHAR(50),
    platform VARCHAR(50),
    session_id VARCHAR(255),
    session_start_time TIMESTAMP,
    version VARCHAR(20),
    source VARCHAR(100),
    environment VARCHAR(50),
    data TEXT
);

-- Create indexes for common query patterns
CREATE INDEX idx_events_event_type ON events(event_type);
CREATE INDEX idx_events_timestamp ON events(event_timestamp);
CREATE INDEX idx_events_user_hash ON events(user_hash);
CREATE INDEX idx_events_session_id ON events(session_id);

-- Create a composite index for time-range queries by event type
CREATE INDEX idx_events_timestamp_type ON events(event_timestamp, event_type);
