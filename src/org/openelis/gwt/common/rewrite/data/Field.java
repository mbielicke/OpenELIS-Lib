package org.openelis.gwt.common.rewrite.data;

import org.openelis.gwt.common.ValidationException;
import org.openelis.gwt.widget.HandlesEvents;

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
        if(value == null)
            return "";
    	return value.toString();
    }
    
    public String toString() {
        if(value == null)
            return "";
    	return value.toString();
    }
    
	public T getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public void setValue(T value) {
	    this.value = value;
	}
	
	public void onValueChange(ValueChangeEvent<T> event) {
		setValue(event.getValue());
		((HasValue)event.getSource()).setValue(format(), false);
	}

}
