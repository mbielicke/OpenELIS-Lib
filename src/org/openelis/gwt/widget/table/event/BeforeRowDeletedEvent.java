package org.openelis.gwt.widget.table.event;

import org.openelis.gwt.widget.table.TableDataRow;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeRowDeletedEvent extends GwtEvent<BeforeRowDeletedHandler> {
	
	private static Type<BeforeRowDeletedHandler> TYPE;
	private int index;
	private TableDataRow row;
	private boolean cancelled;
	
	public static BeforeRowDeletedEvent fire(HasBeforeRowDeletedHandlers source, int index, TableDataRow row) {
		if(TYPE != null) {
			BeforeRowDeletedEvent event = new BeforeRowDeletedEvent(index, row);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeRowDeletedEvent(int index, TableDataRow row) {
		this.row = row;
		this.index = index;
	}

	@Override
	protected void dispatch(BeforeRowDeletedHandler handler) {
		handler.onBeforeRowDeleted(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeRowDeletedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeRowDeletedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeRowDeletedHandler>();
		}
		return TYPE;
	}
	
	public TableDataRow getRow() {
		return row;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
