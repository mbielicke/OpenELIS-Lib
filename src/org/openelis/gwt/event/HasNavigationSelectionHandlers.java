package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasNavigationSelectionHandlers extends HasHandlers {
	
	public HandlerRegistration addNavigationSelectionHandler(NavigationSelectionHandler handler);

}
