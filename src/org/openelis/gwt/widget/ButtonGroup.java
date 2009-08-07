package org.openelis.gwt.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.widget.AppButton.ButtonState;

import java.util.ArrayList;
import java.util.Iterator;

public class ButtonGroup extends Composite implements ClickHandler, HasClickHandlers {
    
    private ArrayList<AppButton> buttons = new ArrayList<AppButton>();
    private AppButton selected;    
       
    public ButtonGroup() {
        
    }
    
    public void setButtons(Panel panel) {
        initWidget(panel);
        findButtons(panel);   
    }
    
    public void findButtons(HasWidgets hw) {
        Iterator<Widget> widsIt = hw.iterator();
        while(widsIt.hasNext()){
            Widget wids = widsIt.next();
            if(wids instanceof AppButton){
                buttons.add((AppButton)wids);
                ((AppButton)wids).addClickHandler(this);
            }else if(wids instanceof HasWidgets) {
                findButtons((HasWidgets)wids);
            }
        }
    }
    
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        for(AppButton button : buttons) {
            button.addClickHandler(handler);
        }
        return null;
    }
    
    public void onClick(ClickEvent event) {
        if(selected != null) {
            selected.changeState(ButtonState.UNPRESSED);
        }
        selected = (AppButton)event.getSource();
        selected.changeState(ButtonState.PRESSED);
    }
    
    public void enable(boolean enabled) {
        for(AppButton button : buttons) {
            if(!enabled)
                button.changeState(ButtonState.DISABLED);
            else
                button.changeState(ButtonState.UNPRESSED);
        }
    }
    
    public void lock() {
        for(AppButton button : buttons) {
            button.lock();
        }
    }
    
    public void unlock() {
        for(AppButton button : buttons) {
            button.unlock();
        }
    }
    
}
