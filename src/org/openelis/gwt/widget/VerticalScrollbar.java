package org.openelis.gwt.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.NativeVerticalScrollbar;
import com.google.gwt.user.client.ui.Widget;

public class VerticalScrollbar extends Composite implements HasScrollHandlers {
	
	/**
	 * Native GWT widget we are wrapping
	 */
	protected NativeVerticalScrollbar bar;
	
	/**
	 * Reference to this object for firing in anonymous class
	 */
	protected VerticalScrollbar source = this;
	
	/**
	 * Semaphore used to determine if event already fired to external listeners
	 */
	protected boolean scrolled;
	
	/**
	 * The current position of the scrollbar.  We save it here because when 
	 * setScrollPosition is called the native bar will not report the correct
	 * scroll position until the end of current event cycle.
	 */
	protected int position,lastFirePos,fireThreshold;
	
	/** 
	 * Constructor to create a VerticalScrollbar
	 */
	public VerticalScrollbar() {
		bar = new NativeVerticalScrollbar();
		
		bar.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				//Update the position of the scroll from user 
				position = bar.getVerticalScrollPosition();
				fireScroll(event.getNativeEvent());
			}
		});
		
		initWidget(bar);
	}
	
	/**
	 * Sets the amount the bar can be scrolled specified in pixels
	 * @param pixels
	 */
	public void  setScrollHeight(int pixels) {
		bar.setScrollHeight(pixels);
	}
	
	/**
	 * Sets the current position of the scroll bar.  Position will be corrected to 
	 * be within the minimum and maximum scroll positions.  A ScrollBarEvent will 
	 * be fired to notify any listeners that a scroll as occurred.
	 * @param pos
	 */
	public void setScrollPosition(int pos) {
		setScrollPosition(pos,true);
	}
	
	/**
	 * Sets the current position of the scroll bar.  Position will be corrected to 
	 * be within the minimum and maximum scroll positions. 
	 * @param pos
	 */
	public void updateScrollPosition(int pos) {
		setScrollPosition(pos,false);
	}
	

	/**
	 * Sets the current position of the scroll bar.  Position will be corrected to 
	 * be within the minimum and maximum scroll positions.  A ScrollBarEvent will 
	 * be fired if passed true in the fire param to notify any listeners that a scroll as occurred.
	 * @param pos
	 */
	protected void setScrollPosition(int pos, boolean fire) {
		
		if(pos == position)
			return;
		
		//Adjust position to be within limits
		if(pos < 0)
			pos = 0;
		else if(pos > bar.getMaximumVerticalScrollPosition())
			pos = bar.getMaximumVerticalScrollPosition();
		
		position = pos;
		
		if(fire)
			fireScroll(Document.get().createScrollEvent());
		
		//Set semaphore to scrolled and set the NativeBar's position
		scrolled = true;
		bar.setVerticalScrollPosition(pos);
	}
	
	/**
	 * Returns the current position in pixels of the scroll bar.
	 * @return
	 */
	public int getScrollPosition() {
		return position;
	}
	
	/**
	 * Method called when setting the position manually or when a user scrolls 
	 * to fire a ScrollBarEvent to all listeners
	 * @param pos
	 */
	private void fireScroll(NativeEvent event) {
		if(!scrolled) {
			if(Math.abs(lastFirePos-position) >= fireThreshold) {
				lastFirePos = position;
				ScrollEvent.fireNativeEvent(event,source);
			}
		}
		scrolled = false;
	}
	
	/**
	 * Method will add a MouseWheelHandler to the passed widget.  Unit represents the 
	 * amount of pixels to scroll with each wheel event.
	 * @param widget
	 * @param unit
	 */
	public void addMouseWheelHandler(Widget widget, final int unit) {
        /*
         * Set MouseWheelHandler to the view and then adjust the scrollBar
         * accordingly
         */
        widget.addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {                
                
            	setScrollPosition(position + (event.getDeltaY() < 0 ? -unit : unit));
            
            }
        }, MouseWheelEvent.getType());
	}
	
	public void setFireThreshold(int thresh) {
		fireThreshold = thresh;
	}
	
	/**
	 * Method used by other objects to register themselves as scroll listener to the scroll bar.
	 */
	@Override
	public HandlerRegistration addScrollHandler(ScrollHandler handler) {
		return addHandler(handler,ScrollEvent.getType());
	}

}
