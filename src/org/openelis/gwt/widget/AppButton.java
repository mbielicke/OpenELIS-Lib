package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DelegatingClickListenerCollection;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.widget.FormInt.State;

import java.util.EnumSet;

public class AppButton extends Composite implements SourcesClickEvents, MouseListener {
    
    private DelegatingClickListenerCollection listeners;
    
    public enum ButtonState {UNPRESSED,PRESSED,DISABLED,LOCK_PRESSED}
    
    public ButtonState state;
    public String action;
    public boolean toggle;
    public EnumSet<State> enabledStates;
    public EnumSet<State> lockedStates;
    private boolean enabled = true;
    private boolean locked = false;
    
    private FocusPanel panel = new FocusPanel();
    private FocusPanel classPanel = new FocusPanel();
    private ClickListener listener;
    private HorizontalPanel hp = new HorizontalPanel();
    private AbsolutePanel right = new AbsolutePanel();
    private AbsolutePanel left = new AbsolutePanel();
    private AbsolutePanel content = new AbsolutePanel();
    
    public AppButton() {
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
    }
    
    public void addClickListener(ClickListener listener) {
        this.listener = listener;
        if(listeners != null && listeners.contains(listener))
            return;
        if(listeners == null){
            listeners = new DelegatingClickListenerCollection(this,panel);
        }
        listeners.add(listener);
    }
    
    public void setWidget(Widget widget) {
        content.add(widget);
    }
    
    public void changeState(ButtonState state){
        this.state = state;
        if(state == ButtonState.UNPRESSED){
            panel.removeStyleName("disabled");
            panel.removeStyleName("Pressed");
            removeClickListener(listener);
            addClickListener(listener);
        }
        if(state == ButtonState.PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
            removeClickListener(listener);
            addClickListener(listener);
        }
        if(state == ButtonState.DISABLED){
            panel.removeStyleName("Pressed");
            panel.addStyleName("disabled");
            removeClickListener(listener);
        }
        if(state == ButtonState.LOCK_PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
            removeClickListener(listener);
        }
    }
    
    public void lock(){
        removeClickListener(listener);
        locked = true;
    }
    
    public void unlock(){
        addClickListener(listener);
        locked = false;
    }

    public void removeClickListener(ClickListener listener) {
        if(listeners != null)
            listeners.remove(listener);
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
    
    public void fireClick() {
        listeners.fireClick(this);
    }
}
