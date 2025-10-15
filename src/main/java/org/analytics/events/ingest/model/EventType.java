package org.analytics.events.ingest.model;

public enum EventType {
    PAGE_VIEW("pageView"),
    USER_ACTION("userAction"),
    ERROR("error"),
    FORM_SUBMIT("formSubmit"),
    API_CALL("apiCall"),
    PERFORMANCE("performance"),
    FEATURE_USAGE("featureUsage"),
    USER_PREFERENCE("userPreference"),
    SEARCH("search"),
    AUTHENTICATION("authentication");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EventType fromValue(String value) {
        for (EventType type : EventType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + value);
    }
}