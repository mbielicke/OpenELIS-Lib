package org.openelis.gwt.screen;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

public class ScreenPanel extends AbsolutePanel implements HasClickHandlers, FocusHandler, BlurHandler, HasFocusHandlers, HasBlurHandlers {
	
	public Widget focused;
	
	public ScreenPanel() {
		
	}
		
	public void addShortcutHandler(ShortcutHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	public void onFocus(FocusEvent event) {
		if(event.getSource() != null){
			focused = (Widget)event.getSource();
		}
		FocusEvent.fireNativeEvent(event.getNativeEvent(), this);
	}

	public void onBlur(BlurEvent event) {

	}
	
	public void setFocusWidget(Widget widget) {
		focused = widget;
		if(widget != null)
			((Focusable)(widget)).setFocus(true);
		FocusEvent.fireNativeEvent(Document.get().createFocusEvent(), this);
	}


	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addHandler(handler,FocusEvent.getType());
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addHandler(handler,BlurEvent.getType());
	}
	
	
	
}

