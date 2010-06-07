package org.openelis.gwt.widget;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;

import com.google.gwt.core.client.GWT;

public class UMonthYearWidget extends Screen {
	
	public UMonthYearWidget() {
		super((ScreenDefInt)GWT.create(MonthYearDef.class));
		initialize();
	}
	
	private void initialize(){
		
	}

	
}
