package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.table.Row;

import com.google.gwt.event.shared.GwtEvent;

public class RowAddedEvent extends GwtEvent<RowAddedHandler> {
	
	private static Type<RowAddedHandler> TYPE;
	private int index;
	private Row row;
	
	public static void fire(HasNodeAddedHandlers source, int index, Row row) {
		if(TYPE != null) {
			RowAddedEvent event = new RowAddedEvent(index, row);
			source.fireEvent(event);
		}
	}
	
	protected RowAddedEvent(int index, Row row) {
		this.row = row;
		this.index = index;
	}

	@Override
	protected void dispatch(RowAddedHandler handler) {
		handler.onRowAdded(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RowAddedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<RowAddedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<RowAddedHandler>();
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
