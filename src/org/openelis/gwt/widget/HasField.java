package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.widget.rewrite.Field;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface HasField<T> {
	
	public Field<T> getField();
	
	public void setField(Field<T> field);
	
	public void addError(String Error);
	
	public void clearErrors();
	
	public void setQueryMode(boolean query);
	
	public void checkValue();
	
	public void getQuery(ArrayList<QueryData> list, String key);
	
	public ArrayList<String> getErrors();
	
	public void enable(boolean enabled);
	
	public boolean isEnabled();
	
	public T getFieldValue();
	
	public void setFieldValue(T value);
	
	public HandlerRegistration addFieldValueChangeHandler(ValueChangeHandler<T> handler);

}
