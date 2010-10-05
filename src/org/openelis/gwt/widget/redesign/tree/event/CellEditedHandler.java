package org.openelis.gwt.widget.redesign.tree.event;

import com.google.gwt.event.shared.EventHandler;

public interface CellEditedHandler extends EventHandler {

	public void onCellUpdated(CellEditedEvent event);
}
