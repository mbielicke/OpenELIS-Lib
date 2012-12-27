package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface DropEnterHandler<I> extends EventHandler {

	public void onDropEnter(DropEnterEvent<I> event);
}
