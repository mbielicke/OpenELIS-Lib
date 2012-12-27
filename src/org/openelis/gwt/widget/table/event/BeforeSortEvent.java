package org.openelis.gwt.widget.table.event;

import org.openelis.gwt.widget.table.event.SortEvent.SortDirection;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeSortEvent extends GwtEvent<BeforeSortHandler> {
	
	private static Type<BeforeSortHandler> TYPE;
	private int index;
	private String colKey;
	private SortDirection direction;
	private boolean cancelled;
	
	public static BeforeSortEvent fire(HasBeforeSortHandlers source, int index, String colKey, SortDirection direction) {		if(TYPE != null) {
			BeforeSortEvent event = new BeforeSortEvent(index,colKey,direction);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeSortEvent(int index, String colKey, SortDirection direction) {
		this.index = index;
		this.colKey = colKey;
		this.direction = direction;
	}

	@Override
	protected void dispatch(BeforeSortHandler handler) {
		handler.onBeforeSort(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeSortHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeSortHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeSortHandler>();
		}
		return TYPE;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getKey() {
		return colKey;
	}
	
	public SortDirection geDirection() {
		return direction;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
