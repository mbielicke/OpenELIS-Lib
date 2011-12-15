package org.openelis.gwt.screen;

import java.util.Arrays;
import java.util.List;

import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.HasField;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class ShortcutHandler implements KeyPressHandler {
	
	char key;
	Widget wid;
	boolean ctrl;
	boolean shift;
	boolean alt;
	protected ScreenPanel panel;
	
	public ShortcutHandler(boolean ctrl,boolean shift,boolean alt,char key,Widget wid) {
		this.ctrl = ctrl;
		this.shift = shift;
		this.alt = alt;
		this.key = key;
		this.wid = wid;
	}
	
	public ShortcutHandler(Node node, Widget wid) {
		String shortcut = node.getAttributes().getNamedItem("shortcut").getNodeValue();
		List<String> keys = Arrays.asList(shortcut.split("\\+"));
		if(keys.contains("ctrl"))
			ctrl = true;
		if(keys.contains("shift"))
			shift = true;
		if(keys.contains("alt"))
			alt = true;
		key = shortcut.charAt(shortcut.length()-1);
		this.wid = wid;
	}
	
	public void onKeyPress(final KeyPressEvent event) {
		if(event.isControlKeyDown() == ctrl && event.isAltKeyDown() == alt && event.isShiftKeyDown() == shift && event.getCharCode() == key){
			if(wid instanceof AppButton) {
				if(((AppButton)wid).isEnabled() && !((AppButton)wid).isLocked()){
					panel.fireChangeEvent();
					((Focusable)wid).setFocus(true);
					DeferredCommand.addCommand(new Command() {
						public void execute() {
					
							NativeEvent clickEvent = com.google.gwt.dom.client.Document.get().createClickEvent(0, 
									wid.getAbsoluteLeft(), 
									wid.getAbsoluteTop(), 
									-1, 
									-1, 
									false, 
									false, 
									false, 
									false);
							
							ClickEvent.fireNativeEvent(clickEvent, (AppButton)wid);
						}
					});
					event.stopPropagation();
				}
				event.preventDefault();
				event.stopPropagation();		
			}else if(((HasField)wid).isEnabled()){ 
				((Focusable)wid).setFocus(true);
				event.preventDefault();
				event.stopPropagation();
			}
		}
	}
}