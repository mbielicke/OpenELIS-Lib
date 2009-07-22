package org.openelis.gwt.widget.tree.rewrite.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeLeafCloseHandler extends EventHandler {

	public void onBeforeLeafClose(BeforeLeafCloseEvent event);
}
