package org.openelis.gwt.widget;

import org.openelis.gwt.event.HasBeforeCloseHandlers;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface WindowInt extends AppStatusInt, HasCloseHandlers<WindowInt>, HasBeforeCloseHandlers<WindowInt>, IsWidget, HasFocusHandlers, Focusable {
	
	public void setContent(final Widget content);
	
	public void setName(String name);
	
	public void close();
	
	public void destroy();
	
	public void makeDragable(DragController controller);
	
	public void positionGlass();

}
