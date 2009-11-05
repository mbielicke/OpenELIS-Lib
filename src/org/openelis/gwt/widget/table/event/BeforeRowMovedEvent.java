package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeRowMovedEvent<T> extends GwtEvent<BeforeRowMovedHandler<T>> {
	
	private static Type<BeforeRowMovedHandler<?>> TYPE;
	private int oldIndex;
	private int newIndex;
	private T row;
	private boolean cancelled;
	
	public static <T> BeforeRowMovedEvent<T> fire(HasBeforeRowMovedHandlers<T> source, int oldIndex, int newIndex, T row) {
		if(TYPE != null) {
			BeforeRowMovedEvent<T> event = new BeforeRowMovedEvent<T>(oldIndex, newIndex, row);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeRowMovedEvent(int oldIndex, int newIndex, T row) {
		this.row = row;
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
	}

	@Override
	protected void dispatch(BeforeRowMovedHandler<T> handler) {
		handler.onBeforeRowMoved(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeRowMovedHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeRowMovedHandler<?>> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeRowMovedHandler<?>>();
		}
		return TYPE;
	}
	
	public T getRow() {
		return row;
	}
	
	public int getOldIndex() {
		return oldIndex;
	}
	
	public int getNewIndex() {
		return newIndex;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
