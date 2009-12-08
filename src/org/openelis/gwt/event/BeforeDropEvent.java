package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public class BeforeDropEvent<I> extends GwtEvent<BeforeDropHandler<I>> {
	
	private static Type<BeforeDropHandler<?>> TYPE;
	private I dragObject;
	private boolean cancelled;
	
	public static <I> BeforeDropEvent<I> fire(HasBeforeDropHandlers<I> source, I dragObject) {
		if(TYPE != null) {
			BeforeDropEvent<I> event = new BeforeDropEvent<I>(dragObject);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeDropEvent(I dragObject) {
		this.dragObject = dragObject;
	}

	@Override
	protected void dispatch(BeforeDropHandler<I> handler) {
		handler.onBeforeDrop(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeDropHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeDropHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<BeforeDropHandler<?>>();
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
