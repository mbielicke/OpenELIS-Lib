package org.openelis.gwt.widget.table.event;

import org.openelis.gwt.widget.table.TableDataRow;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeRowMovedEvent extends GwtEvent<BeforeRowMovedHandler> {
	
	private static Type<BeforeRowMovedHandler> TYPE;
	private int oldIndex;
	private int newIndex;
	private TableDataRow row;
	private boolean cancelled;
	
	public static BeforeRowMovedEvent fire(HasBeforeRowMovedHandlers source, int oldIndex, int newIndex, TableDataRow row) {
		if(TYPE != null) {
			BeforeRowMovedEvent event = new BeforeRowMovedEvent(oldIndex, newIndex, row);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeRowMovedEvent(int oldIndex, int newIndex, TableDataRow row) {
		this.row = row;
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
	}

	@Override
	protected void dispatch(BeforeRowMovedHandler handler) {
		handler.onBeforeRowMoved(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeRowMovedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeRowMovedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeRowMovedHandler>();
		}
		return TYPE;
	}
	
	public TableDataRow getRow() {
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
