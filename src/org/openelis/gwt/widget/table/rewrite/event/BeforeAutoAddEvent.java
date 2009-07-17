package org.openelis.gwt.widget.table.rewrite.event;

import org.openelis.gwt.widget.table.rewrite.TableDataRow;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeAutoAddEvent extends GwtEvent<BeforeAutoAddHandler> {
	
	private static Type<BeforeAutoAddHandler> TYPE;
	private TableDataRow row;
	private boolean cancelled;
	
	public static BeforeAutoAddEvent fire(HasBeforeAutoAddHandlers source, TableDataRow row) {
		if(TYPE != null) {
			BeforeAutoAddEvent event = new BeforeAutoAddEvent(row);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeAutoAddEvent(TableDataRow row) {
		this.row = row;
	}

	@Override
	protected void dispatch(BeforeAutoAddHandler handler) {
		handler.onBeforeAutoAdd(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeAutoAddHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeAutoAddHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeAutoAddHandler>();
		}
		return TYPE;
	}
	
	public TableDataRow getRow() {
		return row;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
