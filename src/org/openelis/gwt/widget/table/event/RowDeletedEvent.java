package org.openelis.gwt.widget.table.event;

import org.openelis.gwt.widget.table.Row;

import com.google.gwt.event.shared.GwtEvent;

public class RowDeletedEvent extends GwtEvent<RowDeletedHandler> {
	
	private static Type<RowDeletedHandler> TYPE;
	private int index;
	private Row row;
	
	public static void fire(HasRowDeletedHandlers source, int index, Row row) {
		if(TYPE != null) {
			RowDeletedEvent event = new RowDeletedEvent(index, row);
			source.fireEvent(event);
		}
	}
	
	protected RowDeletedEvent(int index, Row row) {
		this.row = row;
		this.index = index;
	}

	@Override
	protected void dispatch(RowDeletedHandler handler) {
		handler.onRowDeleted(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RowDeletedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<RowDeletedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<RowDeletedHandler>();
		}
		return TYPE;
	}
	
	public Row getRow() {
		return row;
	}
	
	public int getIndex() {
		return index;
	}
	

}
