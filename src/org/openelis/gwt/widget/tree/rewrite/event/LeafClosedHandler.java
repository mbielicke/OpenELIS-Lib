package org.openelis.gwt.widget.tree.rewrite.event;

import com.google.gwt.event.shared.EventHandler;

public interface LeafClosedHandler extends EventHandler {

	public void onLeafClosed(LeafClosedEvent event);
}
