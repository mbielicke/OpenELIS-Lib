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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.widget.FormInt.State;

import java.util.EnumSet;

public class CommandButton extends Composite implements MouseListener, SourcesCommandEvents, ClickListener {
    

    public CommandListenerCollection commandListeners = new CommandListenerCollection(); 
    
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
    private ClickListener listener;
    private HorizontalPanel hp = new HorizontalPanel();
    private AbsolutePanel right = new AbsolutePanel();
    private AbsolutePanel left = new AbsolutePanel();
    private AbsolutePanel content = new AbsolutePanel();
    
    public CommandButton() {
        hp.add(left);
        hp.add(content);
        hp.add(right);
        classPanel.add(hp);
        panel.add(classPanel);
        initWidget(panel);
        //panel.addStyleName("ScreenPanelButton");
        left.addStyleName("ButtonLeftSide");
        right.addStyleName("ButtonRightSide");
        content.addStyleName("ButtonContent");
        panel.addMouseListener(this);
        panel.addClickListener(this);
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
    
    public void lock(){
        locked = true;
    }
    
    public void unlock(){
        locked = false;
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        if(state != ButtonState.DISABLED && state != ButtonState.LOCK_PRESSED && !locked)
            panel.addStyleName("Hover");
        
    }

    public void onMouseLeave(Widget sender) {
       panel.removeStyleName("Hover");
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        if(toggle && state != ButtonState.DISABLED && state != ButtonState.LOCK_PRESSED &&  !locked){
            if(state == ButtonState.UNPRESSED)
                changeState(ButtonState.PRESSED);
            else
                changeState(ButtonState.UNPRESSED);
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
        commandListeners.fireCommand(command, value);
    }

    public void addCommandListener(CommandListener listener) {
        commandListeners.add(listener);
    }

    public void removeCommandListener(CommandListener listener) {
        commandListeners.remove(listener);
    }


    public void onClick(Widget sender) {
        
            fireCommand();
    }
}
