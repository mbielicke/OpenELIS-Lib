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

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.rewrite.Field;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DelegatingKeyboardListenerCollection;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
@Deprecated
public class CheckBox extends Composite implements ClickListener, SourcesClickEvents, HasValue<String>, Focusable, HasBlurHandlers, HasMouseOutHandlers, HasMouseOverHandlers, HasField<String>{
    
    DelegatingKeyboardListenerCollection keyListeners;
    
    private ClickListenerCollection clickListeners;
    
    boolean enabled = true;
    
    private Field<String> field;
    
    public enum CheckType {TWO_STATE,THREE_STATE};
    
    public static final String UNCHECKED = "N",
                            CHECKED = "Y",
                            UNKNOWN = null;
    
    public static final String UNCHECKED_STYLE = "Unchecked",
                                CHECKED_STYLE = "Checked",
                                UNKNOWN_STYLE = "Unknown";
        
    private String state = UNCHECKED; 
    
    private CheckType type = CheckType.TWO_STATE;
    
    private HorizontalPanel hp = new HorizontalPanel();
    private final CheckBox check = this;
    
    public FocusPanel panel = new FocusPanel() {
        public void onBrowserEvent(Event event) {
            if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_ENTER) {
                check.onClick(check);
            }
            check.onBrowserEvent(event);
            super.onBrowserEvent(event);
        }
    };
    
    public CheckBox() {
        initWidget(hp);
        hp.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        hp.add(panel);
        panel.setStyleName(UNCHECKED_STYLE);
        sinkEvents(Event.KEYEVENTS);
    }
    
    public CheckBox(CheckType type) {
        this();
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setState(UNKNOWN);
    }
    
    public CheckBox(CheckType type, String text){
        this(type);
        Label label = new Label(text);
        label.setStyleName("CheckText");
        hp.add(label);
    }
    
    public CheckBox(CheckType type, Widget widget){
        this(type);
        hp.add(widget);
    }
    
    public void addTabHandler(UIUtil.TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void setText(String text){
        Label label = new Label(text);
        label.setStyleName("CheckText");
        if(hp.getWidgetCount() > 1)
            hp.remove(1);
        hp.add(label);
    }
    
    public void setWidget(Widget widget){
        if(hp.getWidgetCount() > 1)
            hp.remove(1);
        hp.add(widget);
    }
    
    public void setType(CheckType type){
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setState(UNKNOWN);
        else
            setState(UNCHECKED);
    }
    
    public CheckType getType() {
        return type;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
        if(state == CHECKED)
            panel.setStyleName(CHECKED_STYLE);
        else if(state == UNCHECKED) 
            panel.setStyleName(UNCHECKED_STYLE);
        else if(state == UNKNOWN && type == CheckType.THREE_STATE)
            panel.setStyleName(UNKNOWN_STYLE);
        else{
            panel.setStyleName(UNCHECKED_STYLE);
            this.state = UNCHECKED;
        }
    }

    public void onClick(Widget sender) {
       if(type == CheckType.TWO_STATE){
           if(state == CHECKED)
               setState(UNCHECKED);
           else
               setState(CHECKED);
       }else{
           if(state == CHECKED) 
               setState(UNCHECKED);
           else if (state == UNCHECKED)
               setState(UNKNOWN);
           else
               setState(CHECKED);
       }
       
       if(clickListeners != null)
           clickListeners.fireClick(sender);
    }
    
    public void enable(boolean enabled){
        if(enabled){
            panel.removeClickListener(this);
            panel.addClickListener(this);
            panel.sinkEvents(Event.KEYEVENTS);
            enabled = true;
        }else{
            panel.removeClickListener(this);
            panel.unsinkEvents(Event.KEYEVENTS);
            enabled = false;
        }
    }
    
    public void addFocusListener(FocusListener listener){
        panel.addFocusListener(listener);
    }
    
    public void removeFocusListener(FocusListener listener){
        panel.removeFocusListener(listener);
    }
    
    public void setFocus(boolean focus){
        panel.setFocus(focus);
    }
    
    public boolean isEnabled(){
        return enabled;
    }

    public void addClickListener(ClickListener listener) {
        if(clickListeners == null)
            clickListeners = new ClickListenerCollection();
        clickListeners.add(listener);
        
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null)
            clickListeners.remove(listener);
        
    }

	public String getValue() {
		return getState();
	}

	public void setValue(String value) {
		setValue(value,false);
	}

	public void setValue(String value, boolean fireEvents) {
		String old = getState();
		setState(value);
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, value);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}

	public int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setAccessKey(char key) {
		// TODO Auto-generated method stub
		
	}

	public void setTabIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler,BlurEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler,MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler,MouseOverEvent.getType());
	}

	public void addError(String error) {
		field.addError(error);
		field.drawError(this);
	}

	public void clearErrors() {
		field.clearError(this);
	}

	public Field getField() {
		return field;
	}

	public void setField(Field<String> field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
	}

	public void checkValue() {
		field.checkValue(this);
		
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.query = field.queryString;
			qd.key = key;
			qd.type = QueryData.Type.STRING;
			list.add(qd);
		}
		
	}

	public ArrayList<String> getErrors() {
		return field.errors;
	}

	public String getFieldValue() {
		// TODO Auto-generated method stub
		return field.getValue();
	}
	
	public void setFieldValue(String value) {
		field.setValue(value);
		setValue(value);
	}

}
