package org.openelis.gwt.client.widget;

import java.util.HashMap;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ButtonPanel extends Composite implements ClickListener {

	/**
	 * Panel used to display buttons
	 */
    private HorizontalPanel hp = new HorizontalPanel();

    private HashMap buttons = new HashMap();
    
    protected FormInt form;

    public int state = FormInt.DISPLAY;

    public ButtonPanel() {
        initWidget(hp);

    }
    
    public void addButton(AppButton button){
        hp.add(button);
        buttons.put(button.action,button);
        button.addClickListener(this);
    }
    
    public void addWidget(Widget wid){
        hp.add(wid);
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
        if(state == FormInt.ADD){
            setButtonState("query",AppButton.DISABLED);
            setButtonState("update",AppButton.DISABLED);
            setButtonState("delete",AppButton.DISABLED);
            setButtonState("previous",AppButton.DISABLED);
            setButtonState("next",AppButton.DISABLED);
            setButtonState("reload",AppButton.DISABLED);
            setButtonState("select",AppButton.DISABLED);
            setButtonState("commit",AppButton.UNPRESSED);
            setButtonState("abort",AppButton.UNPRESSED);
            setButtonState("add",AppButton.PRESSED);
        }
        if(state == FormInt.QUERY){
            setButtonState("query",AppButton.PRESSED);
            setButtonState("update",AppButton.DISABLED);
            setButtonState("delete",AppButton.DISABLED);
            setButtonState("previous",AppButton.DISABLED);
            setButtonState("next",AppButton.DISABLED);
            setButtonState("reload",AppButton.DISABLED);
            setButtonState("select",AppButton.DISABLED);
            setButtonState("commit",AppButton.UNPRESSED);
            setButtonState("abort",AppButton.UNPRESSED);
            setButtonState("add",AppButton.DISABLED);
        } 
        if(state == FormInt.UPDATE){
            setButtonState("query",AppButton.DISABLED);
            setButtonState("update",AppButton.PRESSED);
            setButtonState("delete",AppButton.DISABLED);
            setButtonState("previous",AppButton.DISABLED);
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
            setButtonState("previous",AppButton.UNPRESSED);
            setButtonState("next",AppButton.UNPRESSED);
            setButtonState("reload",AppButton.UNPRESSED);
            setButtonState("select",AppButton.UNPRESSED);
            setButtonState("commit",AppButton.DISABLED);
            setButtonState("abort",AppButton.DISABLED);
            setButtonState("add",AppButton.UNPRESSED);
        }
        this.state = state;
    }
    
    public void setButtonState(String action, int state) {
        if(buttons.containsKey(action)){
            ((AppButton)buttons.get(action)).changeState(state);
        }
    }
    
    public void removeButton(String action){
        if(buttons.containsKey(action)){
            hp.remove((Widget)buttons.get(action));
            buttons.remove(action);
        }
    }

}
