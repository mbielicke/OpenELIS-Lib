package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class CellEditedEvent extends GwtEvent<CellEditedHandler> {
	
	private static Type<CellEditedHandler> TYPE;
	private int row;
	private int col;
	
	public static void fire(HasCellEditedHandlers source, int row, int col) {
		if(TYPE != null) {
			CellEditedEvent event = new CellEditedEvent(row, col);
			source.fireEvent(event);
		}
	}
	
	protected CellEditedEvent(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	protected void dispatch(CellEditedHandler handler) {
		handler.onCellUpdated(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	public int getCol() {
		return col;
	}
}
