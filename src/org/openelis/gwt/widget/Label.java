package org.openelis.gwt.widget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class Label extends com.google.gwt.user.client.ui.Label implements HasValue<String> {

	public String getValue() {
		return getText();
	}

	public void setValue(String value) {
		setValue(value,false);
	}

	public void setValue(String value, boolean fireEvents) {
		String old = getText();
		setText(value);
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, value);
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}

}
