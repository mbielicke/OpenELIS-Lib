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
    		if((state & ((AppButton)buttons.get(i)).getMaskedEnabledState()) != 0)
    			setButtonState((AppButton) buttons.get(i), AppButton.UNPRESSED);
    		else if((state & ((AppButton)buttons.get(i)).getMaskedLockedState()) != 0)
    			setButtonState((AppButton) buttons.get(i), AppButton.LOCK_PRESSED);
    		else
    			setButtonState((AppButton) buttons.get(i), AppButton.DISABLED);
    		
    		
    	}
        /*if(state == FormInt.ADD){
            setButtonState("query",AppButton.DISABLED);
            setButtonState("update",AppButton.DISABLED);
            setButtonState("delete",AppButton.DISABLED);
            setButtonState("prev",AppButton.DISABLED);
            setButtonState("next",AppButton.DISABLED);
            setButtonState("reload",AppButton.DISABLED);
            setButtonState("select",AppButton.DISABLED);
            setButtonState("commit",AppButton.UNPRESSED);
            setButtonState("abort",AppButton.UNPRESSED);
            setButtonState("add",AppButton.LOCK_PRESSED);
        }
        if(state == FormInt.QUERY){
            setButtonState("query",AppButton.LOCK_PRESSED);
            setButtonState("update",AppButton.DISABLED);
            setButtonState("delete",AppButton.DISABLED);
            setButtonState("prev",AppButton.DISABLED);
            setButtonState("next",AppButton.DISABLED);
            setButtonState("reload",AppButton.DISABLED);
            setButtonState("select",AppButton.DISABLED);
            setButtonState("commit",AppButton.UNPRESSED);
            setButtonState("abort",AppButton.UNPRESSED);
            setButtonState("add",AppButton.DISABLED);
        } 
        if(state == FormInt.UPDATE){
            setButtonState("query",AppButton.DISABLED);
            setButtonState("update",AppButton.LOCK_PRESSED);
            setButtonState("delete",AppButton.DISABLED);
            setButtonState("prev",AppButton.DISABLED);
            setButtonState("next",AppButton.DISABLED);
            setButtonState("reload",AppButton.DISABLED);
            setButtonState("select",AppButton.DISABLED);
            setButtonState("commit",AppButton.UNPRESSED);
            setButtonState("abort",AppButton.UNPRESSED);
            setButtonState("add",AppButton.DISABLED);
        }
        if(state == FormInt.DELETE){
            setButtonState("query",AppButton.DISABLED);
            setButtonState("update",AppButton.DISABLED);
            setButtonState("delete",AppButton.PRESSED);
            setButtonState("prev",AppButton.DISABLED);
            setButtonState("next",AppButton.DISABLED);
            setButtonState("reload",AppButton.DISABLED);
            setButtonState("select",AppButton.DISABLED);
            setButtonState("commit",AppButton.UNPRESSED);
            setButtonState("abort",AppButton.UNPRESSED);
            setButtonState("add",AppButton.DISABLED);
        }
        if(state == FormInt.DISPLAY){
            setButtonState("query",AppButton.UNPRESSED);
            setButtonState("update",AppButton.UNPRESSED);
            setButtonState("delete",AppButton.UNPRESSED);
            setButtonState("prev",AppButton.UNPRESSED);
            setButtonState("next",AppButton.UNPRESSED);
            setButtonState("reload",AppButton.UNPRESSED);
            setButtonState("select",AppButton.UNPRESSED);
            setButtonState("commit",AppButton.DISABLED);
            setButtonState("abort",AppButton.DISABLED);
            setButtonState("add",AppButton.UNPRESSED);
        }*/
        this.state = state;
    }
    
    public void setButtonState(AppButton button, int state) {
        button.changeState(state);
    }
    
    public void setButtonState(String buttonAction, int state){
    	//do nothing for now
    }
    
    public void removeButton(String action){
    /*    if(buttons.containsKey(action)){
            hp.remove((Widget)buttons.get(action));
            buttons.remove(action);
        }*/
    	//do nothing for now
    }

}
