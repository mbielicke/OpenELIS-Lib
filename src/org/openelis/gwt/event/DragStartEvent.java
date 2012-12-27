package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class DragStartEvent<I> extends GwtEvent<DragStartHandler<I>> {
	
	private static Type<DragStartHandler<?>> TYPE;
	private I dragObject;
	private boolean cancelled;
	
	public static <I> DragStartEvent<I> fire(HasDragStartHandlers<I> source, I dragObject) {
		if(TYPE != null) {
			DragStartEvent<I> event = new DragStartEvent<I>(dragObject);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected DragStartEvent(I dragObject) {
		this.dragObject = dragObject;
	}
	
	protected void dispatch(DragStartHandler<I> handler) {
		handler.onDragStart(this);
	}
	
	public final Type<DragStartHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<DragStartHandler<?>> getType() {
	   if (TYPE == null) {
		   TYPE = new Type<DragStartHandler<?>>();
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
	
	
	

}
