/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * This event will be fired before the Drag action starts and can be canceled by the 
 * handler call event.cancel().
 * @author tschmidt
 *
 * @param <I>
 */
public class BeforeDragStartEvent<I> extends GwtEvent<BeforeDragStartHandler<I>> {
	
	private static Type<BeforeDragStartHandler<?>> TYPE;
	private I dragObject;
	private Widget proxy;
	private boolean cancelled;
	
	public static <I> BeforeDragStartEvent<I> fire(HasBeforeDragStartHandlers<I> source, I dragObject) {
		if(TYPE != null) {
			BeforeDragStartEvent<I> event = new BeforeDragStartEvent<I>(dragObject);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeDragStartEvent(I dragObject) {
		this.dragObject = dragObject;
	}
	
	protected void dispatch(BeforeDragStartHandler<I> handler) {
		handler.onBeforeDragStart(this);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final Type<BeforeDragStartHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeDragStartHandler<?>> getType() {
	   if (TYPE == null) {
		   TYPE = new Type<BeforeDragStartHandler<?>>();
	   }
	   return TYPE;
	}
	
	/**
	 * Returns the object that is about to be dragged.
	 * @return
	 */
	public I getDragObject() {
		return dragObject;
	}
	
	/**
	 * Will cancel the Drag if called in the handler.
	 */
	public void cancel() {
		cancelled = true;
	}
	
	/**
	 * Method used to determine if the event is canceled. 
	 * @return
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Setting a widget with this method will cause that widget to be the widget 
	 * dragged on the screen.
	 * @param proxy
	 */
	public void setProxy(Widget proxy) {
		this.proxy = proxy;
	}
	
	/**
	 * Method will return the widget to be used as the display for the drag
	 * @return
	 */
	public Widget getProxy() {
		return proxy;
	}
}
