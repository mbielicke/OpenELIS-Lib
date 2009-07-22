package org.openelis.gwt.widget.tree.rewrite.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasLeafClosedHandlers extends HasHandlers {
	
	public HandlerRegistration addLeafClosedHandler(LeafClosedHandler handler);

}
