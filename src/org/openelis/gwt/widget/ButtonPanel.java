package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.screen.ScreenAppButton;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonPanel extends Composite implements ClickListener {

	/**
	 * Panel used to display buttons
	 */
    private HorizontalPanel hp = new HorizontalPanel();

    private ArrayList buttons = new ArrayList();
    
    protected FormInt form;

    public int state = FormInt.DISPLAY;

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
    
    /**
     * This method sets the Form that this ButtonPanel controls
     * @param form
     */
    public void setForm(FormInt form) {
        this.form = form;
    }

    /**
     * Handler for button clicks
     */
    public void onClick(Widget senderWid) {
    	
        AppButton sender = (AppButton)senderWid;
        if (sender.action.equals("query")) {
            form.query(state);
        }
        else if (sender.action.equals("next")) {
            form.next(state);
        }
        else if (sender.action.equals("prev")) {
            form.prev(state);
        }
        else if (sender.action.equals("add")) {
            form.add(state);
        }
        else if (sender.action.equals("update")) {
            form.up(state);
        }
        else if (sender.action.equals("delete")) {
            form.delete(state);
        }
        else if (sender.action.equals("commit")) {
            form.commit(state);
        }
        else if (sender.action.equals("abort")) {
            form.abort(state);
        }
        else if (sender.action.equals("reload")) {
            form.reload(state);
        }
        else if (sender.action.equals("select")) {
            form.select(state);
        }else
            form.option(sender.action,state);
    }
    
    /**
     * Returns the current state of the form
     * @return state
     */
    public int getState() {
        return state;
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
    	
        this.state = state;
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
}
