package org.openelis.gwt.widget.tree.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasLeafOpenedHandlers extends HasHandlers {
	
	public HandlerRegistration addLeafOpenedHandler(LeafOpenedHandler handler);

}
