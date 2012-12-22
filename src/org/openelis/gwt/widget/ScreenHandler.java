package org.openelis.gwt.widget;

import org.openelis.gwt.common.FieldErrorException;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.TableFieldErrorException;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.widget.table.Table;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

public abstract class ScreenHandler<T> implements ValueChangeHandler<T> {
	
	Widget widget;
	
	public void onValueChange(ValueChangeEvent<T> event) {
		
	}

	public void onDataChange(DataChangeEvent event) {
		
	}

	public void onStateChange(StateChangeEvent event) {

	}
	
	public Widget onTab(boolean shift) {
		return null;
	}

	
	public Object getQuery() {
		if(widget instanceof Queryable)
			if(((Queryable)widget).isQueryMode())
				return ((Queryable)widget).getQuery();
		
		return null;
	}
	
	public boolean isValid() {
		if(widget instanceof HasExceptions)
			return ((HasExceptions)widget).hasExceptions();
		return true;
	}
	
	public void showError(LocalizedException ex) {
		TableFieldErrorException tableE;
		FieldErrorException      fieldE;
	
        if (ex instanceof TableFieldErrorException) {
            tableE = (TableFieldErrorException) ex;
            ((Table)widget).addException(tableE.getRowIndex(),((Table)widget).getColumnByName(tableE.getFieldName()),tableE);
        } else if (ex instanceof FieldErrorException) {
            fieldE = (FieldErrorException)ex;                                      
            ((HasExceptions)widget).addException(fieldE);
        }
	}
	
	public void clearError() {
		if(widget instanceof HasExceptions) 
			((HasExceptions)widget).clearExceptions();
	}

}
