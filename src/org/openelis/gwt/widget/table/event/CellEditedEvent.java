package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class CellEditedEvent extends GwtEvent<CellEditedHandler> {
	
	private static Type<CellEditedHandler> TYPE;
	private int row;
	private int cell;
	private Object value;
	
	public static void fire(HasCellEditedHandlers source, int row, int cell, Object value) {
		if(TYPE != null) {
			CellEditedEvent event = new CellEditedEvent(row, cell, value);
			source.fireEvent(event);
		}
	}
	
	protected CellEditedEvent(int row, int col, Object value) {
		this.row = row;
		this.cell = col;
		this.value = value;
	}

	@Override
	protected void dispatch(CellEditedHandler handler) {
		handler.onCellUpdated(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CellEditedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<CellEditedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<CellEditedHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCell() {
		return cell;
	}
	
	public Object getValue() {
		return value;
	}
 	

}
