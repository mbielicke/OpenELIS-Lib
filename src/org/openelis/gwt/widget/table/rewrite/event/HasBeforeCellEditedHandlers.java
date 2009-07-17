package org.openelis.gwt.widget.table.rewrite.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeCellEditedHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeCellEditedHandler(BeforeCellEditedHandler handler);

}
