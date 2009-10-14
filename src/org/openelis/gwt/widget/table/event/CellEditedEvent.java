package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class CellEditedEvent extends GwtEvent<CellEditedHandler> {
	
	private static Type<CellEditedHandler> TYPE;
	private int tableIndex;
	private int tableCell;
	private int modelIndex;
	private Object value;
	
	public static void fire(HasCellEditedHandlers source, int tableIndex, int cell, int modelIndex, Object value) {
		if(TYPE != null) {
			CellEditedEvent event = new CellEditedEvent(tableIndex, cell, modelIndex, value);
			source.fireEvent(event);
		}
	}
	
	protected CellEditedEvent(int tableIndex, int col, int modelIndex, Object value) {
		this.tableIndex = tableIndex;
		this.tableCell = col;
		this.modelIndex = modelIndex;
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
	
	public int getTableIndex() {
		return tableIndex;
	}
	
	public int getCell() {
		return tableCell;
	}
	
	public int getModelIndex() {
		return modelIndex;
	}
	
	public Object getValue() {
		return value;
	}
 	

}
