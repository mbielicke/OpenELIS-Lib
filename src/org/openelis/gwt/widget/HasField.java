package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.widget.rewrite.Field;

public interface HasField {
	
	public Field getField();
	
	public void setField(Field field);
	
	public void addError(String Error);
	
	public void clearErrors();
	
	public void setQueryMode(boolean query);
	
	public void checkValue();
	
	public void getQuery(ArrayList<QueryData> list, String key);
	
	public ArrayList<String> getErrors();

}
