package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasActionHandlers<I> extends HasHandlers {
	
	public HandlerRegistration addActionHandler(ActionHandler<I> handler);

}
