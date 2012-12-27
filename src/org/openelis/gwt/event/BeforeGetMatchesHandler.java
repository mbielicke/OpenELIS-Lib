package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeGetMatchesHandler extends EventHandler {
	
	public void onBeforeGetMatches(BeforeGetMatchesEvent event);
}
