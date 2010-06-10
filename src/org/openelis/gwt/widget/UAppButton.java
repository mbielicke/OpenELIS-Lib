package org.openelis.gwt.widget;

import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UAppButton extends FocusPanel implements Enable {
	
	private boolean toggles, enabled, pressed;
	
	public UAppButton() {
		init();
	}
	
	public void init() {
		final UAppButton source = this;
		
		addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				addStyleName("Hover");
			}
		});
		
		addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event){
				removeStyleName("Hover");
			}
		});
		
		addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				addStyleName("Hover");
			}
		});
		
		addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				removeStyleName("Hover");
			}
		});
		
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(toggles)
					setPressed(!pressed);
			}
		});
		
		addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event){
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					NativeEvent clickEvent = com.google.gwt.dom.client.Document.get().createClickEvent(0, 
							getAbsoluteLeft(), 
							getAbsoluteTop(), 
							-1, 
							-1, 
							event.isControlKeyDown(), 
							event.isAltKeyDown(), 
							event.isShiftKeyDown(), 
							event.isMetaKeyDown());
					
					ClickEvent.fireNativeEvent(clickEvent, source);
					event.stopPropagation();
				}
			}
		});
		
	}
	
    /**
     * Sinks a KeyPressEvent for this widget attaching a TabHandler that will override the default
     * browser tab order for the tab order defined by the screen for this widget.
     * 
     * @param handler
     *        Instance of TabHandler that controls tabing logic for widget.
     */
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyDownEvent.getType());
    }
    
    /**
     * Can be used to set the look of the Button on the screen.
     * 
     * @param widget
     *        UI widget used for the display of this button
     */
    public void setWidget(Widget widget) {
        setWidget(widget,true);
    }
    
    public void setWidget(Widget widget, boolean wrap) {
    	if(wrap){
    		AbsolutePanel content = new AbsolutePanel();
    		content.add(widget);
    		content.addStyleName("ButtonContent");
    		HorizontalPanel hp = new HorizontalPanel();
    		hp.add(new AbsolutePanel());
    		hp.getWidget(0).addStyleName("ButtonLeftSide");
    		hp.add(content);
    		hp.add(new AbsolutePanel());
    		hp.getWidget(2).addStyleName("ButtonRightSide");
    		super.setWidget(hp);
    	}else
    		super.setWidget(widget);
    	
    	
    }
    
    public void setPressed(boolean pressed) {
    	assert toggles == true;
    	
    	if(this.pressed == pressed)
    		return;
    	
    	this.pressed = pressed;
		if(pressed)
			addStyleName("Pressed");
		else
			removeStyleName("Pressed");
    }
    
    /**
     * Unsinks all events on this button leaving it in it's current state for styling 
     * but removes functionality.
     */
    public void lock(){
        removeStyleName("Hover");
        unsinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
    }
    
    /**
     * Sinks all events on this button to restore functionality leaving the current state
     * styling.
     */
    public void unlock(){
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOUT |Event.ONMOUSEOVER);
    }
    
    /**
     * Call this function with true to enable the button and false to disable.
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
    	if(enabled == isEnabled())
    		return;
    	
    	this.enabled = enabled;
    	if(enabled){
    		unlock();
    		removeStyleName("disabled");
    	}else{
    		lock();
    		addStyleName("disabled");
    	}
    }
    
    /**
     * Method to check if the button is enabled.
     * 
     * @return
     *      True if the button is enabled
     */
    public boolean isEnabled() {
    	return enabled;
    }    

}
