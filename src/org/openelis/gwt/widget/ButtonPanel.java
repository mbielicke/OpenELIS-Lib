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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.AppScreenForm;
import org.openelis.gwt.screen.ScreenAppButton;
import org.openelis.gwt.widget.AppButton.ButtonState;

import java.util.ArrayList;
import java.util.Iterator;

public class ButtonPanel extends Composite implements ClickListener, SourcesChangeEvents, ChangeListener {

    public enum ButtonPanelState {ENABLED,LOCKED} 
    /**
	 * Panel used to display buttons
	 */
    public HorizontalPanel hp = new HorizontalPanel();

    private ArrayList<AppButton> buttons = new ArrayList<AppButton>();
    
    private ChangeListenerCollection changeListeners;
    
    private ButtonPanelState state = ButtonPanelState.ENABLED;
    
    public AppButton buttonClicked;

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
            buttonClicked = (AppButton)sender;
            changeListeners.fireChange(this);
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
    		if(button.getEnabledStates().contains(state) && button.isEnabled())
    			setButtonState(button, ButtonState.UNPRESSED);
    		else if(button.getLockedStates().contains(state) && button.isEnabled())
    			setButtonState(button, ButtonState.LOCK_PRESSED);
    		else
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

    public void addChangeListener(ChangeListener listener) {
       if(changeListeners == null){
           changeListeners = new ChangeListenerCollection();
       }
       changeListeners.add(listener);
        
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null) {
            changeListeners.remove(listener);
        }
        
    }

    public void onChange(Widget sender) {
        if(sender instanceof AppScreenForm){
            setState(((AppScreenForm)sender).state);
        }
    }
    
    public int numberOfButtons(){
        return buttons.size();
    }
}
