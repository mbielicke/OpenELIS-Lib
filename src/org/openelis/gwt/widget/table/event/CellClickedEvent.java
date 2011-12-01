package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class CellClickedEvent extends GwtEvent<CellClickedHandler> {
	
	private static Type<CellClickedHandler> TYPE;
	private int row;
	private int col;
	private boolean cancelled,ctrlKey,shiftKey;
	
	public static CellClickedEvent fire(HasCellEditedHandlers source, int row, int col, boolean ctrlKey, boolean shiftKey) {
		if(TYPE != null) {
			CellClickedEvent event = new CellClickedEvent(row, col,ctrlKey,shiftKey);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected CellClickedEvent(int row, int col,boolean ctrlKey,boolean shiftKey) {
		this.row = row;
		this.col = col;
		this.ctrlKey = ctrlKey;
		this.shiftKey = shiftKey;
	}

	@Override
	protected void dispatch(CellClickedHandler handler) {
		handler.onCellClicked(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CellClickedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<CellClickedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<CellClickedHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public boolean isCtrlKeyDown() {
		return ctrlKey;
	}
	
	public boolean isShiftKeyDown() {
		return shiftKey;
	}

}
