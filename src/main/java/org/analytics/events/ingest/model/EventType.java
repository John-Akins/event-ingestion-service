package org.analytics.events.ingest.model;

/**
 * Enumeration representing different types of events that can be ingested.
 * Each event type has an associated string value.
 */
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

  /**
   * Returns the {@code EventType} enum constant from its string value.
   *
   * @param value The string value of the event type.
   * @return The {@code EventType} enum constant.
   * @throws IllegalArgumentException If an unknown event type value is provided.
   */
  public static EventType fromValue(String value) {
    for (EventType type : EventType.values()) {
      if (type.value.equals(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown event type: " + value);
  }
}
