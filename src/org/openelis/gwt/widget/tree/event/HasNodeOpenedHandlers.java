package org.openelis.gwt.widget.tree.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasNodeOpenedHandlers extends HasHandlers {
	
	public HandlerRegistration addNodeOpenedHandler(NodeOpenedHandler handler);

}
