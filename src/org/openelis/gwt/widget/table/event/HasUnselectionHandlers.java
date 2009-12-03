package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasUnselectionHandlers<I> extends HasHandlers {
	  /**
	   * Adds a {@link SelectionEvent} handler.
	   * 
	   * @param handler the handler
	   * @return the registration for the event
	   */
	  HandlerRegistration addUnselectionHandler(UnselectionHandler<I> handler);
	}
