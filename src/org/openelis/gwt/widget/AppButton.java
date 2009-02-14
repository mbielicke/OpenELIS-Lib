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

import java.util.EnumSet;

import org.openelis.gwt.event.CommandEvent;
import org.openelis.gwt.event.CommandHandler;
import org.openelis.gwt.event.HasCommandHandlers;
import org.openelis.gwt.screen.AppScreenForm.State;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HandlesAllMouseEvents;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AppButton extends Composite implements HasCommandHandlers<String> {
    
    public enum ButtonState {UNPRESSED,PRESSED,DISABLED,LOCK_PRESSED}
    
    public ButtonState state;
    public String value;
    public boolean toggle;
    public EnumSet<State> enabledStates;
    public EnumSet<State> lockedStates;
    private boolean enabled = true;
    private boolean locked = false;
    public Enum command;
    
    private FocusPanel panel = new FocusPanel();
    private FocusPanel classPanel = new FocusPanel();
    private HorizontalPanel hp = new HorizontalPanel();
    private AbsolutePanel right = new AbsolutePanel();
    private AbsolutePanel left = new AbsolutePanel();
    private AbsolutePanel content = new AbsolutePanel();
    private AppButtonHandler handler = new AppButtonHandler();
    
    private class AppButtonHandler extends HandlesAllMouseEvents implements ClickHandler {

		public void onClick(ClickEvent event) {
			if(enabled && !lockedStates.contains(state))
			  fireCommand();
		}

		public void onMouseDown(MouseDownEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void onMouseUp(MouseUpEvent event) {
	        if(toggle && state != ButtonState.DISABLED && state != ButtonState.LOCK_PRESSED &&  !locked){
	            if(state == ButtonState.UNPRESSED)
	                changeState(ButtonState.PRESSED);
	            else
	                changeState(ButtonState.UNPRESSED);
	        }
			
		}

		public void onMouseMove(MouseMoveEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void onMouseOut(MouseOutEvent event) {
			panel.removeStyleName("Hover");
			
		}

		public void onMouseOver(MouseOverEvent event) {
	        if(state != ButtonState.DISABLED && state != ButtonState.LOCK_PRESSED && !locked)
	            panel.addStyleName("Hover");
	        
			
		}

		public void onMouseWheel(MouseWheelEvent event) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
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
        panel.addMouseOutHandler(handler);
        panel.addClickHandler(handler);
        panel.addMouseOverHandler(handler);
        panel.addMouseUpHandler(handler);
    }
    
    
    public void setWidget(Widget widget) {
        content.add(widget);
    }
    
    public void changeState(ButtonState state){
        this.state = state;
        if(state == ButtonState.UNPRESSED){
            panel.removeStyleName("disabled");
            panel.removeStyleName("Pressed");
        }
        if(state == ButtonState.PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
        }
        if(state == ButtonState.DISABLED){
            panel.removeStyleName("Pressed");
            panel.addStyleName("disabled");
        }
        if(state == ButtonState.LOCK_PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
        }
    }
   
    public void setStyleName(String styleName){
	  classPanel.setStyleName(styleName);
    }

	public void setEnabledStates(EnumSet<State> enabledStates) {
		this.enabledStates = enabledStates;
	}

	public void setLockedStates(EnumSet<State> lockedStates) {
		this.lockedStates = lockedStates;
	}

	public EnumSet<State> getEnabledStates() {
		return enabledStates;
	}

	public EnumSet<State> getLockedStates() {
		return lockedStates;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
    
    public void fireCommand() {
    	CommandEvent.fire(this, command, value);
    }

	public HandlerRegistration addCommand(CommandHandler<String> handler) {
		return addHandler(handler, CommandEvent.getType());
	}

}
