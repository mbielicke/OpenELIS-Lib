package org.openelis.gwt.widget;

import java.util.ArrayList;
import java.util.Iterator;

import org.openelis.gwt.screen.AppScreenForm;
import org.openelis.gwt.screen.ScreenAppButton;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

public class ButtonPanel extends Composite implements ClickListener, SourcesChangeEvents, ChangeListener {

    public static final int ENABLED = 0,
                            LOCKED = 1;
	/**
	 * Panel used to display buttons
	 */
    public HorizontalPanel hp = new HorizontalPanel();

    private ArrayList buttons = new ArrayList();
    
    private ChangeListenerCollection changeListeners;
    
    private int state = ENABLED;
    
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
        if(state == ENABLED){
            buttonClicked = (AppButton)sender;
            changeListeners.fireChange(this);
        }else{
            ((AppButton)sender).changeState(AppButton.UNPRESSED);
        }
        
    }
    
    /**
     * Returns the current state of the form
     * @return state
     */
    public int getState() {
        return state;
    }
    
    public void setPanelState(int state){
        if(this.state == state)
            return;
        this.state = state;
        if(state == LOCKED){
            for(int i = 0; i < buttons.size(); i++){
                ((AppButton)buttons.get(i)).lock();
            }
        }else{
            for(int i = 0; i < buttons.size(); i++){
                ((AppButton)buttons.get(i)).unlock();
            }
        }
    }
    /**
     * Sets the ButtonPanel to the state passed in.
     * @param state
     */
    public void setState(int state) {
    	for(int i=0;i<buttons.size();i++){
    		AppButton button = (AppButton)buttons.get(i);
    		if((state & button.getMaskedEnabledState()) != 0 && button.isEnabled())
    			setButtonState(button, AppButton.UNPRESSED);
    		else if((state & button.getMaskedLockedState()) != 0 && button.isEnabled())
    			setButtonState(button, AppButton.LOCK_PRESSED);
    		else
    			setButtonState(button, AppButton.DISABLED);
    	}
    }
    
    public void setButtonState(AppButton button, int state) {
        button.changeState(state);
    }
    
    public void setButtonState(String buttonAction, int state){
    	if(buttonAction != null){
	    	for(int i=0;i<buttons.size();i++){
	    		if(buttonAction.equals(((AppButton)buttons.get(i)).action)){
	    			((AppButton)buttons.get(i)).changeState(state);
	    			break;
	    		}
	    	}	
    	}
    }
    
    public void removeButton(String action){
    	if(action != null){
	    	for(int i=0;i<buttons.size();i++){
	    		if(action.equals(((AppButton)buttons.get(i)).action)){
	    			hp.remove((Widget)((AppButton)buttons.get(i)).getParent());
	    			buttons.remove(i);
	    		}
	    	}
    	}
    }
    
    public void enableButton(String action, boolean enabled){
    	if(action != null){
	    	for(int i=0;i<buttons.size();i++){
	    		if(action.equals(((AppButton)buttons.get(i)).action)){
	    			AppButton button = (AppButton)buttons.get(i);
	    			if(enabled)
	    	            button.changeState(AppButton.UNPRESSED);
	    	        else
	    	            button.changeState(AppButton.DISABLED);
	    			
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
}
