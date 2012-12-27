package org.openelis.gwt.widget.table.event;

import java.util.ArrayList;

import org.openelis.gwt.widget.table.TableDataRow;

import com.google.gwt.event.shared.GwtEvent;

public class TableValueChangeEvent extends GwtEvent<TableValueChangeHandler> {
	
	private static Type<TableValueChangeHandler> TYPE;
	private ArrayList<TableDataRow> model;
	
	public static void fire(HasTableValueChangeHandlers source, ArrayList<TableDataRow> model) {
		if(TYPE != null) {
			TableValueChangeEvent event = new TableValueChangeEvent(model);
			source.fireEvent(event);
		}
	}
	
	protected TableValueChangeEvent(ArrayList<TableDataRow> model) {
		this.model = model;
	}

	@Override
	protected void dispatch(TableValueChangeHandler handler) {
		handler.onTableValueChange(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<TableValueChangeHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<TableValueChangeHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<TableValueChangeHandler>();
		}
		return TYPE;
	}
	
	public ArrayList<TableDataRow> getModel() {
		return model;
	}	

}
