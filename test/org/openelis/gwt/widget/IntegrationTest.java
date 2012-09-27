package org.openelis.gwt.widget;

import org.junit.Ignore;
import org.openelis.gwt.widget.table.Table;
import org.openelis.gwt.widget.table.event.CellClickedEvent;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public abstract class IntegrationTest extends GWTTestCase {

	@Ignore
	public void blur(HasBlurHandlers source) {
		BlurEvent.fireNativeEvent(Document.get().createBlurEvent(),source);
	}
	
	@Ignore
	public void focus(HasFocusHandlers source) {
		FocusEvent.fireNativeEvent(Document.get().createFocusEvent(),source);
	}
	
	@SuppressWarnings("deprecation")
	@Ignore
	public void pressKey(HasHandlers source, int key) {
		KeyDownEvent.fireNativeEvent(Document.get().createKeyDownEvent(false,
																	   false, 
																	   false, 
																	   false, 
																	   key), source);
		KeyUpEvent.fireNativeEvent(Document.get().createKeyUpEvent(false,
																   false,
																   false,
																   false,
																   key), source);
		KeyPressEvent.fireNativeEvent(Document.get().createKeyPressEvent(false, 
																		 false, 
																		 false, 
																		 false,
																		 key,
																		 key),source);
	}
	
	@Ignore
	public void click(HasClickHandlers source) {
		ClickEvent.fireNativeEvent(Document.get().createClickEvent(0,-1,-1,-1,-1,false,false,false,false), source);
	}
	
	@Ignore
	public void clickCell(Table table, int r, int c) {
		CellClickedEvent event = CellClickedEvent.fire(table, r, c, false, false);
		if(event == null || !event.isCancelled())
			table.startEditing(r, c);
	}
	
	@Ignore
	public void setCursorPos(Widget widget, int pos) {
		DOM.setElementProperty(widget.getElement(),"selectionStart",String.valueOf(pos));
		DOM.setElementProperty(widget.getElement(),"selectionEnd",String.valueOf(pos));
	}
	
	
}
