package org.openelis.gwt.widget.redesign.table.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasCellEditedHandlers extends HasHandlers {
	
	public HandlerRegistration addCellEditedHandler(CellEditedHandler handler);

}
