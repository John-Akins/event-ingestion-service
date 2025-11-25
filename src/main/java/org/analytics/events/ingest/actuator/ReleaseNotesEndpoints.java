package org.analytics.events.ingest.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

/**
 * Actuator endpoint for exposing release notes.
 * <p>
 * This class provides a custom actuator endpoint to view release notes. It supports fetching all
 * release notes or specific versions.
 * </p>
 */
@Component
@Endpoint(id = "release")
public class ReleaseNotesEndpoints {

  private static final String VERSION_1_0 =
      "** Version 1.0 ** \n\n" + "* Events ingestion endpoint added \n \n \n";

  /**
   * Returns all available release notes.
   *
   * @return A string containing all release notes.
   */
  @ReadOperation
  public String releaseNotes() {
    return VERSION_1_0;
  }

  /**
   * Returns release notes for a specific version.
   *
   * @param selector The version number to retrieve release notes for (e.g., "1.0").
   * @return A string containing the release notes for the specified version, or all
   *         release notes if the selector is not found.
   */
  @ReadOperation
  public String selectReleaseNotes(@Selector String selector) {
    if ("1.0".equals(selector)) {
      return VERSION_1_0;
    }
    return releaseNotes();
  }
}
