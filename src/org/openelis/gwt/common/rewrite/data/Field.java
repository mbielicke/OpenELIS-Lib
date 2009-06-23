package org.openelis.gwt.common.rewrite.data;

import java.util.ArrayList;

import org.openelis.gwt.common.ValidationException;
import org.openelis.gwt.screen.ScreenTableWidget;
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

public class Field<T> extends HandlesEvents implements ValueChangeHandler<T>, MouseOutHandler, MouseOverHandler, BlurHandler{
	
    public boolean required;
    public boolean valid = true;
    public T value;
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    
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
	    try {
	    	clearError((Widget)event.getSource());
	    	validate();
	    }catch(ValidationException e){
	    	drawError((Widget)event.getSource(),e);
	    }
	}
	
    public void clearError(Widget wid) {
        if(pop != null){
            pop.hide();
        }
        wid.removeStyleName("InputError");
        errorPanel.clear();
    }
    
    public void drawError(Widget wid, ValidationException e) {
        ArrayList<String> errors = new ArrayList<String>();
        errors.add(e.getMessage());
        
        errorPanel.clear();
        for (String error : errors) {
            MenuLabel errorLabel = new MenuLabel(error,"Images/bullet_red.png");
            errorLabel.setStyleName("errorPopupLabel");
            //errorPanel.add(new MenuLabel(error,"Images/bullet_red.png"));
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
		setValue((T)((HasValue)event.getSource()).getValue());
	    try {
	    	clearError((Widget)event.getSource());
	    	validate();
	    }catch(ValidationException e){
	    	drawError((Widget)event.getSource(),e);
	    }
	}

}
