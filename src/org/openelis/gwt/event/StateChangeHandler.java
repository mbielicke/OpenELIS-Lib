package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface StateChangeHandler<I> extends EventHandler {
	
	public void onStateChange(StateChangeEvent<I> event);

}
