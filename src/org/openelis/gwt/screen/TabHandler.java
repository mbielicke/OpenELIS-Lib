package org.openelis.gwt.screen;

import org.openelis.gwt.widget.DeckPanel;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.ScreenWidgetInt;
import org.openelis.gwt.widget.TabPanel;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class TabHandler implements KeyDownHandler {
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
	
	public void onKeyDown(KeyDownEvent event) {
		if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB){
			if(((ScreenWidgetInt)event.getSource()).isEnabled() ||  !def.getWidget(wid).getElement().equals(event.getRelativeElement())){
				ScreenWidgetInt nextWid = null;
				if(event.isShiftKeyDown()){
					if(def.getWidget(prev) instanceof TabPanel)
						nextWid = (ScreenWidgetInt)def.getWidget(((TabPanel)def.getWidget(prev)).getPrevTabWidget());
					else if(def.getWidget(prev) instanceof DeckPanel)
						nextWid = (ScreenWidgetInt)def.getWidget(((DeckPanel)def.getWidget(prev)).getPrevTabWidget());
					else 
						nextWid = (ScreenWidgetInt)def.getWidget(prev);
					if(nextWid.isEnabled()) {
						def.getPanel().setFocusWidget((Widget)nextWid);
						//if(nextWid instanceof Focusable)
							//((Focusable)nextWid).setFocus(true);
					}else{
						KeyPressEvent.fireNativeEvent(event.getNativeEvent(), (HasHandlers)def.getWidget(prev), def.getWidget(wid).getElement());
					}
				}else{
					if(def.getWidget(next) instanceof TabPanel)
						nextWid = (ScreenWidgetInt)def.getWidget(((TabPanel)def.getWidget(next)).getNextTabWidget());
					else if(def.getWidget(next) instanceof DeckPanel)
						nextWid = (ScreenWidgetInt)def.getWidget(((DeckPanel)def.getWidget(next)).getNextTabWidget());
					else
						nextWid = (ScreenWidgetInt)def.getWidget(next);
					if(nextWid.isEnabled()) {
						def.getPanel().setFocusWidget((Widget)nextWid);
						//if(nextWid instanceof Focusable)
							//((Focusable)nextWid).setFocus(true);
					}else{
						KeyPressEvent.fireNativeEvent(event.getNativeEvent(), (HasHandlers)def.getWidget(next), def.getWidget(wid).getElement());
					}							
				}
			}
			event.preventDefault();
			event.stopPropagation();
		}
	}
}
