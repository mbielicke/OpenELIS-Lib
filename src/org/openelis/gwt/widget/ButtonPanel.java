/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.event.CommandListener;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.SourcesCommandEvents;
import org.openelis.gwt.screen.ScreenAppButton;
import org.openelis.gwt.widget.AppButton.ButtonState;

import java.util.ArrayList;
import java.util.Iterator;

public class ButtonPanel extends Composite implements ClickListener, SourcesCommandEvents, CommandListener {

    public enum ButtonPanelState {ENABLED,LOCKED}
    public enum Action {ADD,UPDATE,DELETE,NEXT,PREVIOUS,RELOAD,QUERY,COMMIT,ABORT,SELECT,OPTION}
    /**
	 * Panel used to display buttons
	 */
    public HorizontalPanel hp = new HorizontalPanel();

    private ArrayList<AppButton> buttons = new ArrayList<AppButton>();
    
    private CommandListenerCollection commandListeners;
    
    private ButtonPanelState state = ButtonPanelState.ENABLED;
    
    public ButtonPanel() {
        initWidget(hp);

    }
    
    public void addButton(AppButton button){
        hp.add(button);
        buttons.add(button);
        button.addClickListener(this);
    }
    
    public void addWidget(Widget wid){
        hp.add(wid);
        if(wid instanceof ScreenAppButton){
            buttons.add((AppButton)((ScreenAppButton)wid).getWidget());
            ((AppButton)((ScreenAppButton)wid).getWidget()).addClickListener(this);
        }
    }
    
    public void findButtons(HasWidgets hw) {
        Iterator widsIt = hw.iterator();
        while(widsIt.hasNext()){
            Widget wids = (Widget)widsIt.next();
            if(wids instanceof ScreenAppButton){
                buttons.add((AppButton)((ScreenAppButton)wids).getWidget());
                ((AppButton)((ScreenAppButton)wids).getWidget()).addClickListener(this);
            }else if(wids instanceof HasWidgets) {
                findButtons((HasWidgets)wids);
            }
        }
    }

    /**
     * Handler for button clicks
     */
    public void onClick(Widget sender) {
        if(state == ButtonPanelState.ENABLED){
            Action action = Action.valueOf(((AppButton)sender).action.toUpperCase().split(":")[0]);
            if(commandListeners != null)
                commandListeners.fireCommand(action,sender);
        }else{
            ((AppButton)sender).changeState(ButtonState.UNPRESSED);
        }
        
    }
    
    /**
     * Returns the current state of the form
     * @return state
     */
    public ButtonPanelState getState() {
        return state;
    }
    
    public void setPanelState(ButtonPanelState state){
        if(this.state == state)
            return;
        this.state = state;
        if(state == ButtonPanelState.LOCKED){
            for(AppButton button : buttons) {
                button.lock();
            }
        }else{
            for(AppButton button : buttons) {
                button.unlock();
            }
        }
    }
    /**
     * Sets the ButtonPanel to the state passed in.
     * @param state
     */
    public void setState(FormInt.State state) {
    	for(AppButton button : buttons) {
    		if(button.getEnabledStates().contains(state) && button.isEnabled()){
                if(button.state == AppButton.ButtonState.LOCK_PRESSED)
                    setButtonState(button, ButtonState.PRESSED);
                else if(button.state != AppButton.ButtonState.PRESSED)
                    setButtonState(button, ButtonState.UNPRESSED);
                if(button.toggle && state == FormInt.State.DEFAULT)
                    setButtonState(button, ButtonState.UNPRESSED);
            }
    		else if(button.getLockedStates().contains(state) && button.isEnabled() && button.state == AppButton.ButtonState.PRESSED)
    			setButtonState(button, ButtonState.LOCK_PRESSED);
    		else if(!button.getEnabledStates().contains(state) && button.isEnabled())
    			setButtonState(button, ButtonState.DISABLED);
            
    	}
    }
    
    public void setButtonState(AppButton button, ButtonState state) {
        button.changeState(state);
    }
    
    public void setButtonState(String buttonAction, ButtonState state){
    	if(buttonAction != null){
	    	for(AppButton button : buttons) { 
	    		if(buttonAction.equals(button.action)){
	    			button.changeState(state);
	    			break;
	    		}
	    	}	
    	}
    }
    
    public void removeButton(String action){
    	if(action != null){
	    	for(AppButton button : buttons) {
	    		if(action.equals(button.action)){
	    			hp.remove((Widget)button.getParent());
	    			buttons.remove(button);
	    		}
	    	}
    	}
    }
    
    public void enableButton(String action, boolean enabled){
    	if(action != null){
	    	for(AppButton button : buttons) {
	    		if(action.equals(button.action)){
	    			if(enabled)
	    	            button.changeState(ButtonState.UNPRESSED);
	    	        else
	    	            button.changeState(ButtonState.DISABLED);
	    			
	    			button.setEnabled(enabled);
	    			break;
	    		}
	    	}
    	}    	
    }

    public void addCommandListener(CommandListener listener) {
       if(commandListeners == null){
           commandListeners = new CommandListenerCollection();
       }
       commandListeners.add(listener);
        
    }

    public void removeCommandListener(CommandListener listener) {
        if(commandListeners != null) {
            commandListeners.remove(listener);
        }
        
    }
    
    public boolean canPerformCommand(Enum action, Object obj){
        return (action.getDeclaringClass() == FormInt.State.class);
    }

    public void performCommand(Enum action, Object obj) {
        if(action.getDeclaringClass() == FormInt.State.class)
            setState((FormInt.State)action);
    }
    
    public int numberOfButtons(){
        return buttons.size();
    }
}
