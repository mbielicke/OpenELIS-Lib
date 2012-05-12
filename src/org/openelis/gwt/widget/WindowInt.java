package org.openelis.gwt.widget;

import org.openelis.gwt.event.HasBeforeCloseHandlers;

import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.user.client.ui.Widget;

public interface WindowInt extends AppStatusInt, HasCloseHandlers<WindowInt>, HasBeforeCloseHandlers<WindowInt> {
	
	public void setContent(final Widget content);
	
	public void setName(String name);
	
	public void close();
	
	public void destroy();

}
