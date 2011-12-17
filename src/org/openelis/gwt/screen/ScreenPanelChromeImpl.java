package org.openelis.gwt.screen;

import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.ScreenWidgetInt;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Focusable;

public class ScreenPanelChromeImpl extends ScreenPanel {
	
	public ScreenPanelChromeImpl() {
		super();
		System.out.println("Using Screen Chrome");
	}
	
	public ScreenPanelChromeImpl(ScreenDefInt def) {
		System.out.println("Using Screen Chrome");
	}
	
	public void setKeyHandling() {
		addDomHandler(new KeyDownHandler() {
			public void onKeyDown(final KeyDownEvent event) {
				boolean ctrl,alt,shift;
				char key;
				
				/*
				 * If no modifier is pressed then return out
				 */
				if(!event.isAnyModifierKeyDown())
					return;
				
				ctrl = event.isControlKeyDown();
				alt = event.isAltKeyDown();
				shift = event.isShiftKeyDown();
				key = (char)event.getNativeKeyCode();
				System.out.println("In PanelChrome KeyDown handler");
				System.out.println(ctrl +","+alt+","+shift+","+key+" shortcuts = "+shortcuts);
				for(final Shortcut handler : shortcuts) {
					if(handler.ctrl == ctrl && handler.alt == alt && handler.shift == shift && String.valueOf(handler.key).toUpperCase().equals(String.valueOf(key).toUpperCase())){
						if(handler.wid instanceof Button) {
							if(((Button)handler.wid).isEnabled() && !((Button)handler.wid).isLocked()){
								fireChange();
								((Focusable)handler.wid).setFocus(true);
								Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
									public void execute() {
										NativeEvent clickEvent = Document.get().createClickEvent(0, 
												-1, 
												-1, 
												-1, 
												-1, 
												false, 
												false, 
												false, 
												false);
									    
										ClickEvent.fireNativeEvent(clickEvent, (Button)handler.wid);
									}
								});

								event.stopPropagation();
							}
							event.preventDefault();
							event.stopPropagation();
						}else if(((ScreenWidgetInt)handler.wid).isEnabled()){ 
							((Focusable)handler.wid).setFocus(true);
							event.preventDefault();
							event.stopPropagation();
						}
					}
				}
			}
		},KeyDownEvent.getType());
	}

}
