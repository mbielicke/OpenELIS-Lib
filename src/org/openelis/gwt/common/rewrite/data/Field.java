package org.openelis.gwt.common.rewrite.data;

import org.openelis.gwt.widget.HandlesEvents;
import org.openelis.util.ValidationException;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

public class Field<T> extends HandlesEvents implements ValueChangeHandler<T>{
	
    public boolean required;
    public boolean valid = true;
    public T value;
    
    public void validate() throws ValidationException {
    	
    }
    
    public String format() {
    	return value.toString();
    }
    
    public String toString() {
    	return value.toString();
    }
    
	public T getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public void setValue(T value) {
		setValue(value);
	}
	
	public void onValueChange(ValueChangeEvent<T> event) {
		setValue(event.getValue());
		((HasValue)event.getSource()).setValue(format(), false);
	}

}
