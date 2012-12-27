package org.openelis.gwt.widget.table.event;

import org.openelis.gwt.widget.table.TableDataRow;

import com.google.gwt.event.shared.GwtEvent;

public class RowMovedEvent extends GwtEvent<RowMovedHandler> {
	
	private static Type<RowMovedHandler> TYPE;
	private int oldIndex;
	private int newIndex;
	private TableDataRow row;
	
	public static void fire(HasRowMovedHandlers source, int oldIndex, int newIndex, TableDataRow row) {
		if(TYPE != null) {
			RowMovedEvent event = new RowMovedEvent(oldIndex, newIndex, row);
			source.fireEvent(event);
		}
	}
	
	protected RowMovedEvent(int oldIndex, int newIndex, TableDataRow row) {
		this.row = row;
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
	}

	@Override
	protected void dispatch(RowMovedHandler handler) {
		handler.onRowMoved(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RowMovedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<RowMovedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<RowMovedHandler>();
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
	

}
