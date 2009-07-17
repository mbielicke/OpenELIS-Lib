package org.openelis.gwt.widget.table.rewrite.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeRowAddedHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeRowAddedHandler(BeforeRowAddedHandler handler);
	

}
