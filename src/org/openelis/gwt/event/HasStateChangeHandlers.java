package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasStateChangeHandlers<I> extends HasHandlers {
	
	HandlerRegistration addStateChangeHandler(StateChangeHandler<I> handler);
}
