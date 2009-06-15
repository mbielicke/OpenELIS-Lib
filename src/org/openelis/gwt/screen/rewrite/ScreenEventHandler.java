package org.openelis.gwt.screen.rewrite;

import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.event.StateChangeHandler;
import org.openelis.gwt.screen.rewrite.Screen.State;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class ScreenEventHandler<Type> implements ValueChangeHandler<Type>, DataChangeHandler, StateChangeHandler<Screen.State>, ActionHandler<Screen.State> {
	
	public void onValueChange(ValueChangeEvent<Type> event) {
		
	}

	public void onDataChange(DataChangeEvent event) {
		
	}

	public void onStateChange(StateChangeEvent<State> event) {

	}

	public void onAction(ActionEvent<State> event) {
		
	}	

}
