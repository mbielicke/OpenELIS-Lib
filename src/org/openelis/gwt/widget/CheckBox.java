package org.openelis.gwt.widget;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DelegatingKeyboardListenerCollection;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CheckBox extends Composite implements ClickListener{
    
    DelegatingKeyboardListenerCollection keyListeners;
    
    public static final String UNCHECKED = "N",
                            CHECKED = "Y",
                            UNKNOWN = null;
    
    public static final String UNCHECKED_STYLE = "Unchecked",
                                CHECKED_STYLE = "Checked",
                                UNKNOWN_STYLE = "Unknown";
    
    public static final int TWO_STATE = 0,
                            THREE_STATE = 1;
    
    private String state = UNCHECKED; 
    
    private int type = TWO_STATE;
    
    private HorizontalPanel hp = new HorizontalPanel();
    private final CheckBox check = this;
    
    public FocusPanel panel = new FocusPanel() {
        public void onBrowserEvent(Event event) {
            check.onBrowserEvent(event);
        }
    };
    
    public CheckBox() {
        initWidget(hp);
        hp.add(panel);
        panel.setStyleName(UNCHECKED_STYLE);
    }
    
    public CheckBox(int type) {
        this();
        this.type = type;
        if(type == THREE_STATE)
            setState(UNKNOWN);
    }
    
    public CheckBox(int type, String text){
        this(type);
        Label label = new Label(text);
        label.setStyleName("CheckText");
        hp.add(label);
    }
    
    public CheckBox(int type, Widget widget){
        this(type);
        hp.add(widget);
    }
    
    public void setText(String text){
        Label label = new Label(text);
        label.setStyleName("CheckText");
        if(hp.getWidgetCount() > 1)
            hp.remove(1);
        hp.add(label);
    }
    
    public void setWidget(Widget widget){
        if(hp.getWidgetCount() > 1)
            hp.remove(1);
        hp.add(widget);
    }
    
    public void setType(int type){
        this.type = type;
        if(type == THREE_STATE)
            setState(UNKNOWN);
        else
            setState(UNCHECKED);
    }
    
    public int getType() {
        return type;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
        if(state == CHECKED)
            panel.setStyleName(CHECKED_STYLE);
        else if(state == UNCHECKED) 
            panel.setStyleName(UNCHECKED_STYLE);
        else if(state == UNKNOWN)
            panel.setStyleName(UNKNOWN_STYLE);
    }

    public void onClick(Widget sender) {
       if(type == TWO_STATE){
           if(state == CHECKED)
               setState(UNCHECKED);
           else
               setState(CHECKED);
       }else{
           if(state == CHECKED) 
               setState(UNCHECKED);
           else if (state == UNCHECKED)
               setState(UNKNOWN);
           else
               setState(CHECKED);
       }
    }
    
    public void enable(boolean enabled){
        if(enabled){
            panel.removeClickListener(this);
            panel.addClickListener(this);
            panel.sinkEvents(Event.KEYEVENTS);
        }else{
            panel.removeClickListener(this);
            panel.unsinkEvents(Event.KEYEVENTS);
        }
    }
    
    public void addFocusListener(FocusListener listener){
        panel.addFocusListener(listener);
    }
    
    public void removeFocusListener(FocusListener listener){
        panel.removeFocusListener(listener);
    }
    
    public void setFocus(boolean focus){
        panel.setFocus(focus);
    }
    

}
