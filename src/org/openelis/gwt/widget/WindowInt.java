package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.event.HasBeforeCloseHandlers;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface WindowInt extends HasCloseHandlers<WindowInt>, HasBeforeCloseHandlers<WindowInt>, IsWidget, HasFocusHandlers, Focusable {
	
	public void setMessagePopup(ArrayList<LocalizedException> exceptions, String style);
	
	public void clearMessagePopup(String style);
	
	public void setStatus(String text, String style);
	
	public void lockWindow();
	
	public void unlockWindow();
	
	public void setBusy();
	
	public void setBusy(String message);
	
	public void clearStatus();
	
	public void setDone(String message);
	
	public void setError(String message);	
	
	public void setContent(final Widget content);
	
	public void setName(String name);
	
	public void close();
	
	public void destroy();
	
	public void makeDragable(DragController controller);
	
	public void positionGlass();

}
