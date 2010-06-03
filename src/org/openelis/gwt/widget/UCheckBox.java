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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;


public class UCheckBox extends FocusPanel implements ScreenWidgetInt<UCheckBox.CheckState> {
        
    boolean enabled = true;
        
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
    };
        
    private CheckState value; 
    
    protected boolean queryMode;
    
    private CheckType type = CheckType.TWO_STATE;
    
    private final UCheckBox check = this;
        
    public UCheckBox() {
        
        setValue(CheckState.UNCHECKED);
        
        addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               if(!enabled)
                   return;
              if(type == CheckType.TWO_STATE){
                  if(value == CHECKED)
                      setValue(UNCHECKED,true);
                  else
                      setValue(CHECKED,true);
              }else{
                  if(value == CHECKED) 
                      setValue(UNCHECKED,true);
                  else if (value == UNCHECKED)
                      setValue(UNKNOWN,true);
                  else
                      setValue(CHECKED,true);
              }
              addStyleName("Focus");
            } 
        });
        
        addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if(!enabled)
                        return;
                   if(type == CheckType.TWO_STATE){
                       if(value == CHECKED)
                           setValue(UNCHECKED,true);
                       else
                           setValue(CHECKED,true);
                   }else{
                       if(value == CHECKED) 
                           setValue(UNCHECKED,true);
                       else if (value == UNCHECKED)
                           setValue(UNKNOWN,true);
                       else
                           setValue(CHECKED,true);
                   }
                   addStyleName("Focus");
                }
            }
        });
        
        /*
         * Unsink these events so the will not fire until widget enabled
         */
        unsinkEvents(Event.ONCLICK | Event.ONKEYDOWN);
    }
    
    public UCheckBox(CheckType type) {
        this();
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setValue(UNKNOWN);
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyDownEvent.getType());
    }
    
    public void setType(CheckType type){
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setValue(CheckState.UNKNOWN);
        else
            setValue(CheckState.UNCHECKED);
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
        setValue(UNKNOWN);
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
        return value;
    }

    public void setValue(String value) {
        setValue(value,false);
    }

    public void setValue(String value, boolean fireEvents) {
        String old;
        
        old = value;
        this.value = value;
        setStyle
    }

    public void addException(LocalizedException exception) {
        // TODO Auto-generated method stub
        
    }

    public void addExceptionStyle(String style) {
        // TODO Auto-generated method stub
        
    }

    public void clearExceptions() {
        // TODO Auto-generated method stub
        
    }

    public ArrayList<LocalizedException> getEndUserExceptions() {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<LocalizedException> getValidateExceptions() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasExceptions() {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeExceptionStyle(String style) {
        // TODO Auto-generated method stub
        
    }
    

}
