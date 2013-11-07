package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.ui.common.Warning;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Field<T> extends HandlesEvents implements ValueChangeHandler<String>, MouseOutHandler, MouseOverHandler, BlurHandler, HasValueChangeHandlers<T> {
	
    public boolean required;
    public boolean valid = true;
    public T value;
    protected VerticalPanel exceptionPanel = new VerticalPanel();
    protected PopupPanel pop;
    public boolean queryMode;
    public String queryString;
    public ArrayList<Exception> exceptions;
    public boolean drawErrors = true;
    
    public void validate() {
    	
    }
    
    public String format() {
    	if(queryMode){
    		if(queryString == null)
    			return "";
    		return queryString;
    	}
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
		if(queryMode){
			queryString = getString(value);
			validateQuery();
		}else
			this.value = value;
	}
	
	public void setStringValue(String value) {
		
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
		clearExceptions((HasField)event.getSource());
		if(queryMode) {
			queryString = getString(event.getValue());
			validateQuery();
		}else{
			valid = true;
			setStringValue(getString(event.getValue()));
			if(!valid){
				drawExceptions((HasField)event.getSource());
				return;
			}
			((HasValue)event.getSource()).setValue(format(), false);
			validate();
		}
		if(!valid){
			drawExceptions((HasField)event.getSource());
		}
		if(!queryMode){
			final HasValueChangeHandlers source = this;
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					ValueChangeEvent.fire(source, value);
				}
			});
		}
			
	}
	
	protected String getString(Object val) {
		if(val == null)
			return "";
		return val.toString();
	}
	
    public void clearExceptions(HasField wid) {
        if(pop != null){
            pop.hide();
        }
        wid.removeExceptionStyle("InputError");
        wid.removeExceptionStyle("InputWarning");
        exceptionPanel.clear();
        exceptions = null;
        valid = true;
    }
    
    public void addException(Exception e){
    	if(exceptions == null)
    		exceptions = new ArrayList<Exception>();
    	
    	//Check for repeating exceptions
    	for(int i = 0; i < exceptions.size(); i++){
            if(exceptions.get(i).getMessage().equals(e.getMessage()))
                return;
        }
    	
    	if(!exceptions.contains(e))
    		exceptions.add(e);
    }
    
    protected void removeException(String key) {
    	if(exceptions == null)
    		return;
    	for(int i = 0; i < exceptions.size(); i++){
    		if(exceptions.get(i).getMessage().equals(key))
    			exceptions.remove(i);
    	}
    	if(exceptions.size() == 0)
    		exceptions = null;
    }
    
    public void drawExceptions(HasField wid) {     
    	if(!drawErrors)
    		return;
        exceptionPanel.clear();
    	wid.removeExceptionStyle("InputWarning");
    	wid.removeExceptionStyle("InputError");
        if(exceptions == null || exceptions.size() == 0){
        	exceptions = null;
        	return;
        }
        String style = "InputWarning";
        for (Exception exception : exceptions) {
        	HorizontalPanel hp = new HorizontalPanel();
        	if(exception instanceof Warning) {
        		hp.add(new Image("/openelis/openelis/Images/bullet_yellow.png"));
        		hp.setStyleName("warnPopupLabel");
        	}else{
        		hp.add(new Image("/openelis/openelis/Images/bullet_red.png"));
        		hp.setStyleName("errorPopupLabel");
        		style = "InputError";
        	}
        	hp.add(new Label(exception.getMessage()));
            
            exceptionPanel.add(hp);
        }
        wid.addExceptionStyle(style);
    }
    
	public void onMouseOut(MouseOutEvent event) {
	    String styleName = ((Widget)event.getSource()).getStyleName();
        if(exceptions != null && exceptions.size() > 0){
            if(pop != null){
                pop.hide();
            }
        }
	}

	public void onMouseOver(MouseOverEvent event) {
        if(exceptions != null && exceptions.size() > 0 && drawErrors) {
            if(pop == null){
                pop = new PopupPanel(true);
                pop.setStyleName("");
            }
            drawExceptions((HasField)event.getSource());
            DecoratorPanel dp = new DecoratorPanel();            
            dp.setStyleName("ErrorWindow");
            dp.add(exceptionPanel);
            dp.setVisible(true);
            pop.setWidget(dp);
            pop.setPopupPosition(((Widget)event.getSource()).getAbsoluteLeft()+((Widget)event.getSource()).getOffsetWidth(), ((Widget)event.getSource()).getAbsoluteTop());
            pop.show();
            Timer timer = new Timer() {
            	public void run() {
            		pop.hide();
            	}
            };
            timer.schedule(5000);
        }
	}

	public void onBlur(BlurEvent event) {
		if(((HasField)event.getSource()).isEnabled())
			checkValue((HasField)event.getSource());
	}
	
	public void checkValue(HasField wid) {
		if(queryMode){
			if(((HasValue)wid).getValue() != null && !((HasValue)wid).getValue().equals("")){
				queryString = ((HasValue)wid).getValue().toString();
				validateQuery();
			}else
				queryString = null;
		}else{
			Object value = wid.getWidgetValue();
			if(value == null)
				value = "";
			setStringValue(value.toString());
			
			validate();
		}
		if(!valid){
			drawExceptions(wid);
		}
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}

}
