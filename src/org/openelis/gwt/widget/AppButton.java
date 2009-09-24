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

import org.openelis.gwt.screen.ShortcutHandler;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.screen.UIUtil;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AppButton extends Composite implements MouseOutHandler, MouseOverHandler, HasClickHandlers, ClickHandler {     
    
    public enum ButtonState {UNPRESSED,PRESSED,DISABLED,LOCK_PRESSED}
    
    public ButtonState state = ButtonState.UNPRESSED;
    public String action;
    public boolean toggle;
    private FocusPanel panel = new FocusPanel();
    private FocusPanel classPanel = new FocusPanel();
    private HorizontalPanel hp = new HorizontalPanel();
    private AbsolutePanel right = new AbsolutePanel();
    private AbsolutePanel left = new AbsolutePanel();
    private AbsolutePanel content = new AbsolutePanel();
    private boolean enabled = true;
    private ShortcutHandler shortcut;
    
    public AppButton() {
        hp.add(left);
        hp.add(content);
        hp.add(right);
        classPanel.add(hp);
        panel.add(classPanel);
        initWidget(panel);
        left.addStyleName("ButtonLeftSide");
        right.addStyleName("ButtonRightSide");
        content.addStyleName("ButtonContent");
        panel.addMouseOutHandler(this);
        panel.addMouseOverHandler(this);
        addClickHandler(this);
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void setWidget(Widget widget) {
        content.add(widget);
    }
    
    public void changeState(ButtonState state){
        this.state = state;
         if(state == ButtonState.UNPRESSED){
            panel.removeStyleName("disabled");
            panel.removeStyleName("Pressed");
            unlock();
        }
        if(state == ButtonState.PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
            unlock();
        }
        if(state == ButtonState.DISABLED){
            panel.removeStyleName("Pressed");
            panel.addStyleName("disabled");
            lock();
        }
        if(state == ButtonState.LOCK_PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
            lock();
        }
    }
    
    public void lock(){
        unsinkEvents(Event.ONCLICK);
        unsinkEvents(Event.ONMOUSEOUT);
        unsinkEvents(Event.ONMOUSEOVER);
    }
    
    public void unlock(){
        sinkEvents(Event.ONCLICK);
        sinkEvents(Event.ONMOUSEOUT);
        sinkEvents(Event.ONMOUSEOVER);
        
    }
   
    public void setStyleName(String styleName){
	  classPanel.setStyleName(styleName);
    }

	public HandlerRegistration addClickHandler(ClickHandler handler){
	    return addHandler(handler, ClickEvent.getType());
	}

    public void onMouseOut(MouseOutEvent event) {
        panel.removeStyleName("Hover");
    }

    public void onMouseOver(MouseOverEvent event) {
        if(enabled)
            panel.addStyleName("Hover");
    }
    
    public void enable(boolean enabled) {
    	this.enabled = enabled;
    	if(enabled)
    		changeState(ButtonState.UNPRESSED);
    	else
    		changeState(ButtonState.DISABLED);
    }
    
    public boolean isEnabled() {
    	return enabled;
    }
    
    public void onClick(ClickEvent event) {
    	if(toggle) {
    		if(state == ButtonState.UNPRESSED)
    			changeState(ButtonState.PRESSED);
    		else if(state == ButtonState.PRESSED)
    			changeState(ButtonState.UNPRESSED);
    	}
    }
    

}
