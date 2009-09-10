package org.openelis.gwt.screen.rewrite;

import org.openelis.gwt.widget.HasField;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;
import com.google.gwt.xml.client.Node;

public class TabHandler implements KeyPressHandler {
	String next;
	String prev;
	ScreenDefInt def;
	String wid;
	
	public TabHandler(Node node, ScreenDefInt def) {
		String tab = node.getAttributes().getNamedItem("tab").getNodeValue();
		String[] tabs = tab.split(",");
		next = tabs[0];
		prev = tabs[1];
		wid = node.getAttributes().getNamedItem("key").getNodeValue();
		this.def = def;
		
	}
	
	public TabHandler(String next, String prev, ScreenDefInt def, String wid){
		this.next = next;
		this.prev = prev;
		this.def = def;
		this.wid = wid;
	}
	
	public void onKeyPress(KeyPressEvent event) {
		if(event.getNativeEvent().getKeyCode() == KeyboardHandler.KEY_TAB){
			if(((HasField)event.getSource()).isEnabled() ||  !def.getWidget(wid).getElement().equals(event.getRelativeElement())){
				if(event.isShiftKeyDown()){
					if(((HasField)def.getWidget(prev)).isEnabled()) 
						((Focusable)def.getWidget(prev)).setFocus(true);
					else{
						KeyPressEvent.fireNativeEvent(event.getNativeEvent(), (HasHandlers)def.getWidget(prev), def.getWidget(wid).getElement());
					}
				}else{
					if(((HasField)def.getWidget(next)).isEnabled()) 
						((Focusable)def.getWidget(next)).setFocus(true);
					else{
						KeyPressEvent.fireNativeEvent(event.getNativeEvent(), (HasHandlers)def.getWidget(next), def.getWidget(wid).getElement());
					}							
				}
			}
			event.preventDefault();
			event.stopPropagation();
		}
	}
}
