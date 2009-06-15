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
package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;
import java.util.Iterator;

import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.CommandListenerCollection;
import org.openelis.gwt.event.HasActionHandlers;
import org.openelis.gwt.screen.ScreenAppButton;
import org.openelis.gwt.screen.rewrite.Screen.State;
import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.AppButton.ButtonState;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonPanel extends Composite implements ClickListener, ClickHandler, HasActionHandlers<ButtonPanel.Action> {

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
            if(wids instanceof AppButton){
                buttons.add((AppButton)wids);
                ((AppButton)wids).addClickListener(this);
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
            ActionEvent.fire(this, action, sender);
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
    public void setState(State state) {
    	for(AppButton button : buttons) {
    		if(button.getEnabledStates().contains(state) && button.isEnabled()){
                if(button.state == AppButton.ButtonState.LOCK_PRESSED)
                    setButtonState(button, ButtonState.PRESSED);
                else if(button.state != AppButton.ButtonState.PRESSED)
                    setButtonState(button, ButtonState.UNPRESSED);
                if(button.toggle && state == State.DEFAULT)
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

    
    public int numberOfButtons(){
        return buttons.size();
    }

	public void onClick(ClickEvent event) {
        if(state == ButtonPanelState.ENABLED){
            Action action = Action.valueOf(((AppButton)event.getSource()).action.toUpperCase().split(":")[0]);
            ActionEvent.fire(this, action, null);
        }else{
            ((AppButton)event.getSource()).changeState(ButtonState.UNPRESSED);
        }
		
	}

	public HandlerRegistration addActionHandler(ActionHandler<Action> handler) {
		// TODO Auto-generated method stub
		return addHandler(handler, ActionEvent.getType());
	}

}
