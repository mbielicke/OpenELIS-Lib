package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeCellEditedEvent extends GwtEvent<BeforeCellEditedHandler> {
	
	private static Type<BeforeCellEditedHandler> TYPE;
	private int row;
	private int col;
	private Object value;
	private boolean cancelled;
	
	public static BeforeCellEditedEvent fire(HasBeforeCellEditedHandlers source, int row, int cell, Object value) {
		if(TYPE != null) {
			BeforeCellEditedEvent event = new BeforeCellEditedEvent(row, cell, value);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeCellEditedEvent(int row, int col, Object value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}

	@Override
	protected void dispatch(BeforeCellEditedHandler handler) {
		handler.onBeforeCellEdited(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeCellEditedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeCellEditedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeCellEditedHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
 	

}
