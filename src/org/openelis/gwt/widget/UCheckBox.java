/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;


public class UCheckBox extends Composite implements ScreenWidgetInt<String>, Focusable {
        
    protected boolean enabled = true;
    
    protected AbsolutePanel panel; 
    protected ArrayList<LocalizedException> endUserExceptions;
    protected ArrayList<LocalizedException> validateExceptions;
        
    public enum CheckType {TWO_STATE,THREE_STATE};
    
    public enum CheckState { 
        UNCHECKED ("N","Unchecked"),
        CHECKED   ("Y","Checked"),
        UNKNOWN   (null,"Unknown");
    
        private String value;
        private String style;
                            
        CheckState(String value, String style) {
            this.value = value;
            this.style = style;
        }
                             
        public String getValue() {
            return value;
        }
                            
        public String getStyle() {
            return style;
        }                      
        
        public static CheckState getState(String value){
        	if(value.equals("Y"))
        		return CheckState.CHECKED;
        	else if(value.equals("N"))
        		return CheckState.UNCHECKED;
        	else
        		return CheckState.UNKNOWN;
        }
    };
        
    private CheckState value = CheckState.UNKNOWN; 
    
    protected boolean queryMode;
    
    private CheckType type = CheckType.TWO_STATE;
    
    private final UCheckBox check = this;
        
    public UCheckBox() {
    	
    	panel = new AbsolutePanel();
    	initWidget(panel);
    	
        setValue(CheckState.UNCHECKED.getValue());
        
        addHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
        	   changeValue();
            } 
        },ClickEvent.getType());
        
        addDomHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                	changeValue();
                }
            }
        },KeyDownEvent.getType());
        
        /*
         * Unsink these events so the will not fire until widget enabled
         */
        //unsinkEvents(Event.ONCLICK);
        unsinkEvents(Event.ONKEYDOWN);
    }
    
    public UCheckBox(CheckType type) {
        this();
        setType(type);
    }
    
    private void changeValue() {
 	   switch(value) {
	    case CHECKED  :
	    	if(type == CheckType.THREE_STATE)
	    		setValue(CheckState.UNKNOWN.getValue(),true);
	    	else
	    		setValue(CheckState.UNCHECKED.getValue(),true);
	    	break;
	  	case UNCHECKED :
	  		setValue(CheckState.CHECKED.getValue(),true);
	  		break;
	  	case UNKNOWN :
	  		setValue(CheckState.UNCHECKED.getValue(),true);
	  		break;
	  };
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyDownEvent.getType());
    }
    
    public void setType(CheckType type){
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setValue(CheckState.UNKNOWN.getValue());
        else
            setValue(CheckState.UNCHECKED.getValue());
    }
    
    public CheckType getType() {
        return type;
    }

    public void addFocusStyle(String style) {
       addStyleName(style);
    }

    public Object getQuery() {
        QueryData qd;
        
        if(value == CheckState.UNKNOWN)
           return null;
        
        qd = new QueryData();
        qd.type = QueryData.Type.STRING;
        qd.query = value.getValue();
       
        return qd;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void removeFocusStyle(String style) {
        removeStyleName(style);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(enabled)
            sinkEvents(Event.ONCLICK | Event.ONKEYDOWN);
        else
            unsinkEvents(Event.ONCLICK | Event.ONKEYDOWN);   
    }

    /**
     * Stub Impelementation for ScreenWidgetInt
     */
    public void setHelper(WidgetHelper<String> helper) {}

    
    public void setQueryMode(boolean query) {
        if(queryMode == query)
            return;
        setType(CheckType.THREE_STATE);
    }

    /**
     * Stub
     */
    public void setRequired(boolean required) {}


    /**
     * Stub
     */
    public void validateValue() {   
        
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public String getValue() {
        return value.getValue();
    }

    public void setValue(String value) {
        setValue(value,false);
    }

    public void setValue(String val, boolean fireEvents) {
        String old;
        
        old = value.getValue();
        value = CheckState.getState(val);
        panel.setStylePrimaryName(value.getStyle());
        
        if(fireEvents) 
        	ValueChangeEvent.fireIfNotEqual(this, old, value.getValue());
    }

    /**
     * Convenience method to check if a widget has exceptions so we do not need
     * to go through the cost of merging the logical and validation exceptions
     * in the getExceptions method.
     * 
     * @return
     */
    public boolean hasExceptions() {
        return endUserExceptions != null || validateExceptions != null;
    }

    /**
     * Adds a manual Exception to the widgets exception list.
     */
    public void addException(LocalizedException error) {
        if (endUserExceptions == null)
            endUserExceptions = new ArrayList<LocalizedException>();
        endUserExceptions.add(error);
        ExceptionHelper.getInstance().checkExceptionHandlers(this);
    }

    protected void addValidateException(LocalizedException error) {
        if (validateExceptions == null)
            validateExceptions = new ArrayList<LocalizedException>();
        validateExceptions.add(error);
    }

    /**
     * Combines both exceptions list into a single list to be displayed on the
     * screen.
     */
    public ArrayList<LocalizedException> getValidateExceptions() {
        return validateExceptions;
    }

    public ArrayList<LocalizedException> getEndUserExceptions() {
        return endUserExceptions;
    }

    /**
     * Clears all manual and validate exceptions from the widget.
     */
    public void clearExceptions() {
        endUserExceptions = null;
        validateExceptions = null;
        ExceptionHelper.getInstance().checkExceptionHandlers(this);
    }

    /**
     * Will add the style to the widget.
     */
    public void addExceptionStyle(String style) {
        addStyleName(style);
    }

    /**
     * will remove the style from the widget
     */
    public void removeExceptionStyle(String style) {
        removeStyleName(style);
    }

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler,BlurEvent.getType());
	}
	
	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler,FocusEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return null;
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAccessKey(char key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus(boolean focused) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTabIndex(int index) {
		// TODO Auto-generated method stub
		
	}
    

}
