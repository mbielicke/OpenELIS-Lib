package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.widget.HandlesEvents;
import org.openelis.gwt.widget.MenuLabel;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Field<T> extends HandlesEvents implements ValueChangeHandler<String>, MouseOutHandler, MouseOverHandler, BlurHandler{
	
    public boolean required;
    public boolean valid = true;
    public T value;
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    public ArrayList<String> errors;
    public boolean queryMode;
    public String queryString;
    
    public void validate() {
    	
    }
    
    public String format() {
        if(value == null)
            return "";
    	return value.toString();
    }
    
    public String formatQuery() {
    	return format();
    }
    
    public void validateQuery() {
    	
    }
    
    public void setQueryMode(boolean query) {
    	queryMode = query;
    	queryString = null;
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
	
	public void setStringValue(String value) {
		
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
		clearError((Widget)event.getSource());
		if(queryMode) {
			queryString = event.getValue();
			validateQuery();
		}else{
			valid = true;
			setStringValue(event.getValue());
			if(!valid){
				drawError((Widget)event.getSource());
				return;
			}
			((HasValue)event.getSource()).setValue(format(), false);
			validate();
		}
		if(!valid){
			drawError((Widget)event.getSource());
		}
	}
	
    public void clearError(Widget wid) {
        if(pop != null){
            pop.hide();
        }
        wid.removeStyleName("InputError");
        errorPanel.clear();
        errors = null;
    }
    
    public void addError(String error){
    	if(errors == null)
    		errors = new ArrayList<String>();
    	errors.add(error);
    }
    
    public void drawError(Widget wid) {        
        errorPanel.clear();
        for (String error : errors) {
            MenuLabel errorLabel = new MenuLabel(error,"Images/bullet_red.png");
            errorLabel.setStyleName("errorPopupLabel");
            errorPanel.add(errorLabel);
        }
        if(errors.size() == 0){
            wid.removeStyleName("InputError");
        }else{
            wid.addStyleName("InputError");
        }
        
    }
    
	public void onMouseOut(MouseOutEvent event) {
        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
            if(pop != null){
                pop.hide();
            }
        }
	}

	public void onMouseOver(MouseOverEvent event) {
        if(((Widget)event.getSource()).getStyleName().indexOf("InputError") > -1){
            if(pop == null){
                pop = new PopupPanel();
                //pop.setStyleName("ErrorPopup");
            }
            DecoratorPanel dp = new DecoratorPanel();
            
            //ScreenWindow win = new ScreenWindow(pop,"","","",false);
            dp.setStyleName("ErrorWindow");
            dp.add(errorPanel);
            dp.setVisible(true);
            pop.setWidget(dp);
            pop.setPopupPosition(((Widget)event.getSource()).getAbsoluteLeft()+((Widget)event.getSource()).getOffsetWidth(), ((Widget)event.getSource()).getAbsoluteTop());
            pop.show();
        }
	}

	public void onBlur(BlurEvent event) {
		checkValue((Widget)event.getSource());
	}
	
	public void checkValue(Widget wid) {
		clearError(wid);
		if(queryMode){
			if(((HasValue)wid).getValue() != null && !((HasValue)wid).getValue().equals("")){
				queryString = ((HasValue)wid).getValue().toString();
				validateQuery();
			}else
				queryString = null;
		}else{
			valid = true;
			Object value = ((HasValue)wid).getValue();
			if(value == null)
				value = "";
			setStringValue(value.toString());
			if(!valid){
				drawError(wid);
				return;
			}
			validate();
		}
		if(!valid){
			drawError(wid);
		}
	}

}
