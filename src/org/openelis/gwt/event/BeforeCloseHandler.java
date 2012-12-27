package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeCloseHandler<T> extends EventHandler {
	
	public void onBeforeClosed(BeforeCloseEvent<T> event);
}
