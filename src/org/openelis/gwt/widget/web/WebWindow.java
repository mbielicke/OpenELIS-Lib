package org.openelis.gwt.widget.web;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.event.BeforeCloseHandler;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.widget.Confirm;
import org.openelis.gwt.widget.ScreenWindow;
import org.openelis.gwt.widget.ScreenWindowInt;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class WebWindow extends FocusPanel implements ScreenWindowInt {
	
	protected AbsolutePanel glass;
	protected Widget content;
	private Confirm confirm;
	
	public WebWindow() {
	}

	public HandlerRegistration addBeforeClosedHandler(
			BeforeCloseHandler<ScreenWindow> handler) {
		//Do Nothing
		return null;
	}

	public void fireEvent(GwtEvent<?> event) {
		//Do Nothing
	}

	public void setContent(Widget content) {
		setWidget(content);
		this.content = content;
		if(content instanceof Screen) {
			((Screen)content).setWindowInt(this);
			setName(((Screen)content).getDefinition().getName());
		}	
	}

	public void setName(String name) {

	}

	public void close() {
		// Do Nothing
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void setMessagePopup(ArrayList<LocalizedException> exceptions,
			String style) {
	}

	public void clearMessagePopup(String style) {
	}

	public void setStatus(String text, String style) {
	}

	public void lockWindow() {
    	if(glass == null) {
    		glass = new AbsolutePanel();
    		glass.setStyleName("GlassPanel");
    		glass.setHeight(content.getOffsetHeight()+"px");
    		glass.setWidth(content.getOffsetWidth()+"px");
    		RootPanel.get().add(glass, content.getAbsoluteLeft(),content.getAbsoluteTop());
    	}
	}

	public void unlockWindow() {
    	if(glass != null) {
    		glass.removeFromParent();
    		glass = null;
    	}
	}

	public void setBusy() {
		setBusy("");
	}

	public void setBusy(String message) {
		confirm = new Confirm(Confirm.Type.BUSY,null,message,null);
		confirm.show(-1,80);
	}

	public void clearStatus() {
		if(confirm != null)
			confirm.hide();
	}

	public void setDone(String message) {
		clearStatus();
	}

	public void setError(String message) {
		confirm = new Confirm(Confirm.Type.ERROR,"Error",message,"OK");
		confirm.show(-1,80);
	}

	public void setProgress(int percent) {

	}
}
