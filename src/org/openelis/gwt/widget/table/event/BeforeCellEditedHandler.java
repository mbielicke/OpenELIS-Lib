package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeCellEditedHandler extends EventHandler {

	public void onBeforeCellEdited(BeforeCellEditedEvent event);
}
