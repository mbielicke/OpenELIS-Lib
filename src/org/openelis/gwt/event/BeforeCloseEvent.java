package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeCloseEvent<T> extends GwtEvent<BeforeCloseHandler<T>> {
	
	private static Type<BeforeCloseHandler<?>> TYPE;
	private T target;
	private boolean cancelled;
	
	public static <T> BeforeCloseEvent<T> fire(HasBeforeCloseHandlers<T> source, T target) {
		if(TYPE != null) {
			BeforeCloseEvent<T> event = new BeforeCloseEvent<T>(target);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeCloseEvent(T target) {
		this.target = target;
	}

	@Override
	protected void dispatch(BeforeCloseHandler<T> handler) {
		handler.onBeforeClosed(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeCloseHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeCloseHandler<?>> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeCloseHandler<?>>();
		}
		return TYPE;
	}
	
	public T getTarget() {
		return target;
	}
		
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
