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

public class AppButton extends Composite implements SourcesClickEvents, MouseListener {
    
    private DelegatingClickListenerCollection listeners;
    
    public static final int UNPRESSED = 0,
                            PRESSED = 1,
                            DISABLED = 2,
                            LOCK_PRESSED = 3;
    
    public int state;
    public String action;
    public boolean toggle;
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
        if(listeners == null){
            listeners = new DelegatingClickListenerCollection(this,panel);
        }
        listeners.add(listener);
    }
    
    public void setWidget(Widget widget) {
        content.add(widget);
    }
    
    public void changeState(int state){
        this.state = state;
        if(state == UNPRESSED){
            panel.removeStyleName("disabled");
            panel.removeStyleName("Pressed");
            removeClickListener(listener);
            addClickListener(listener);
        }
        if(state == PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
            removeClickListener(listener);
            addClickListener(listener);
        }
        if(state == DISABLED){
            panel.removeStyleName("Pressed");
            panel.addStyleName("disabled");
            removeClickListener(listener);
        }
        if(state == LOCK_PRESSED){
            removeClickListener(listener);
        }
    }

    public void removeClickListener(ClickListener listener) {
        if(listeners != null)
            listeners.remove(listener);
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        if(state != DISABLED && state != LOCK_PRESSED)
            panel.addStyleName("Hover");
        
    }

    public void onMouseLeave(Widget sender) {
       panel.removeStyleName("Hover");
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        if(toggle && state != DISABLED && state != LOCK_PRESSED){
            if(state == UNPRESSED)
                changeState(PRESSED);
            else
                changeState(UNPRESSED);
        }
    }
   
    public void setStyleName(String styleName){
	  classPanel.setStyleName(styleName);
    }
}
