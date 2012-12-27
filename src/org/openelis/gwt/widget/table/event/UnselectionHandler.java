package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.EventHandler;

public interface UnselectionHandler<I> extends EventHandler {

	  /**
	   * Called when {@link SelectionEvent} is fired.
	   * 
	   * @param event the {@link SelectionEvent} that was fired
	   */
	  void onUnselection(UnselectionEvent<I> event);
	}
