package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

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
	
	public final Type<BeforeDragStartHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeDragStartHandler<?>> getType() {
	   if (TYPE == null) {
		   TYPE = new Type<BeforeDragStartHandler<?>>();
	   }
	   return TYPE;
	}
	
	public I getDragObject() {
		return dragObject;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setProxy(Widget proxy) {
		this.proxy = proxy;
	}
	
	public Widget getProxy() {
		return proxy;
	}
	
	
	

}
