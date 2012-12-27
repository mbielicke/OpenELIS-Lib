package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeCloseHandlers<T> extends HasHandlers {
	
	public HandlerRegistration addBeforeClosedHandler(BeforeCloseHandler<T> handler);
	

}
