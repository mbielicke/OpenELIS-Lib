package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface ActionHandler<I> extends EventHandler {
	
	public void onAction(ActionEvent<I> event);

}
