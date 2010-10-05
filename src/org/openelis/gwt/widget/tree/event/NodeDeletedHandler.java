package org.openelis.gwt.widget.tree.event;

import com.google.gwt.event.shared.EventHandler;

public interface NodeDeletedHandler extends EventHandler {
	
	public void onNodeDeleted(NodeDeletedEvent event);
}
