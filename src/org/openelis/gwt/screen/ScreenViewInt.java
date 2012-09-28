package org.openelis.gwt.screen;

import java.util.ArrayList;
import java.util.HashMap;

import org.openelis.gwt.common.ValidationErrorsList;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.HasDataChangeHandlers;
import org.openelis.gwt.event.HasStateChangeHandlers;
import org.openelis.gwt.widget.WindowInt;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;


public interface ScreenViewInt extends IsWidget,HasDataChangeHandlers,HasStateChangeHandlers {
	 
    public Focusable tab(Focusable widget,boolean shift);
    
    public Focusable shortcut(boolean ctrl, boolean shift, boolean alt, char key);
    
    public void finishEditing();
    
    public boolean validate();
    
    public void showErrors(ValidationErrorsList list,WindowInt window);
    
    public void clearErrors(WindowInt window);
    
    public ArrayList<QueryData> getQueryFields();
    
    public <T> T getWidget(String key);
    
    public HashMap<String,Widget> getWidgets();
	
    public <T> void addScreenHandler(Widget widget, ScreenEventHandler<T> handler);
}
