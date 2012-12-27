package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDataChangeHandlers extends HasHandlers {
	
	HandlerRegistration addDataChangeHandler(DataChangeHandler handler);

}
