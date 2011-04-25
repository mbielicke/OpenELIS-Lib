package org.openelis.gwt.widget.table.event;

import org.openelis.gwt.widget.table.Row;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author tschmidt
 *
 */
public class BeforeRowAddedEvent extends GwtEvent<BeforeRowAddedHandler> {
	
	private static Type<BeforeRowAddedHandler> TYPE;
	private int index;
	private Row row;
	private boolean cancelled;
	
	public static BeforeRowAddedEvent fire(HasBeforeRowAddedHandlers source, int index, Row row) {
		if(TYPE != null) {
			BeforeRowAddedEvent event = new BeforeRowAddedEvent(index, row);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeRowAddedEvent(int index, Row row) {
		this.row = row;
		this.index = index;
	}

	@Override
	protected void dispatch(BeforeRowAddedHandler handler) {
		handler.onBeforeRowAdded(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeRowAddedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeRowAddedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeRowAddedHandler>();
		}
		return TYPE;
	}
	
	public Row getRow() {
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
