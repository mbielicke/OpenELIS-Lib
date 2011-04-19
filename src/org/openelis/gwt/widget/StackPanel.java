package org.openelis.gwt.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class StackPanel extends com.google.gwt.user.client.ui.StackPanel {
	
	@Override
	public void add(Widget w, String stackText) {
		// TODO Auto-generated method stub
		super.add(w, stackText);
		if(!isAttached()) {
		    UIObject.setVisible(DOM.getParent(getWidget(getWidgetCount()-1).getElement()),true);
		    getWidget(getWidgetCount()-1).setVisible(true);
		}
	}
	
	@Override
	protected void onAttach() {
	    boolean firstAttach = !isOrWasAttached();
		super.onAttach();
	
		if(firstAttach) {
		    for(int i = 1; i < getWidgetCount(); i++) {
		        UIObject.setVisible(DOM.getParent(getWidget(i).getElement()),false);
		        getWidget(i).setVisible(false);
		    }
		}
	}

}
