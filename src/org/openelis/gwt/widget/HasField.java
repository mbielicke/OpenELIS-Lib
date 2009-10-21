package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface HasField<T> {
	
	public Field<T> getField();
	
	public void setField(Field<T> field);
	
	public void addException(LocalizedException exception);
	
	public void clearExceptions();
	
	public void setQueryMode(boolean query);
	
	public void checkValue();
	
	public void getQuery(ArrayList<QueryData> list, String key);
	
	public ArrayList<LocalizedException> getExceptions();
	
	public void enable(boolean enabled);
	
	public boolean isEnabled();
	
	public T getFieldValue();
	
	public void setFieldValue(T value);
	
	public HandlerRegistration addFieldValueChangeHandler(ValueChangeHandler<T> handler);

}
