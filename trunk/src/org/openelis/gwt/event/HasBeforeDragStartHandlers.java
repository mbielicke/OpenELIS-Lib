package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeDragStartHandlers<I> extends HasHandlers {

	public HandlerRegistration addBeforeStartHandler(BeforeDragStartHandler<I> handler);
}
