package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class SortEvent extends GwtEvent<SortHandler> {
	
	private static Type<SortHandler> TYPE;
	private int index;
	private String colKey;
	private SortDirection direction;
	public enum SortDirection {ASCENDING,DESCENDING}
	
	public static void fire(HasSortHandlers source, int index, String colKey, SortDirection direction) {
		if(TYPE != null) {
			SortEvent event = new SortEvent(index,colKey,direction);
			source.fireEvent(event);
		}
	}
	
	protected SortEvent(int index, String colKey, SortDirection direction) {
		this.index = index;
		this.colKey = colKey;
		this.direction = direction;
	}

	@Override
	protected void dispatch(SortHandler handler) {
		handler.onSort(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SortHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<SortHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<SortHandler>();
		}
		return TYPE;
	}
	
	public String getKey() {
		return colKey;
	}
	
	public int getIndex() {
		return index;
	}
	
	public SortDirection getDirection() {
		return direction;
	}
	

}
