package org.analytics.events.ingest.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;


@Component
@Endpoint(id="release")
public class ReleaseNotesEndpoints {
	
	private static String version10 =  "** Version 1.0 ** \n\n"
			+ "* Events ingestion endpoint added \n"
			+ "\n"
			+ "\n";	

	@ReadOperation
	public String releaseNotes() {
		return version10;
	}
	
	@ReadOperation
	public String selectReleaseNotes(@Selector String selector) {
		if("1.0".equals(selector)) return version10;
		else return releaseNotes();
	}
}

