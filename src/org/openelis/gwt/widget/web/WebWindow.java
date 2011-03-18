/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget.web;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.event.BeforeCloseHandler;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.widget.Confirm;
import org.openelis.gwt.widget.WindowInt;

import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is an implementation of the ScreenWindowInt to be used by the Web portal of OpenELIS
 *
 */
public class WebWindow extends FocusPanel implements WindowInt {
	
	protected AbsolutePanel glass,title,contentPanel;
	protected Widget content;
	private Confirm confirm;
	private VerticalPanel vp;
	private Label name;
	
	/**
	 * No-Arg constructor that sets up the skeleton of the Window.
	 */
	public WebWindow() {
		vp = new VerticalPanel();
		title = new AbsolutePanel();
		name = new Label();
		
		title.setStyleName("crumbline");
		name.setStyleName("webLabel");
		title.add(name);
		
		contentPanel = new AbsolutePanel();
		contentPanel.setWidth("100%");
		contentPanel.setHeight("100%");
		
		vp.add(title);
		vp.add(contentPanel);
		vp.setWidth("100%");
		vp.setHeight("100%");
		vp.setCellWidth(contentPanel, "100%");
		vp.setCellHeight(contentPanel,"100%");
		setWidget(vp);
		
	}

	/**
	 * Stub Method implemented to satisfy interface
	 */
	public HandlerRegistration addBeforeClosedHandler(
			BeforeCloseHandler<WindowInt> handler) {
		//Do Nothing
		return null;
	}

	/**
	 * Stub method implemented to satisfy interface
	 */
	public void fireEvent(GwtEvent<?> event) {
		//Do Nothing
	}

	/**
	 * Method is called when the windows content is changed through user actions
	 */
	public void setContent(Widget content) {
		contentPanel.clear();
		contentPanel.add(content);
		this.content = content;
		if(content instanceof Screen) {
			((Screen)content).setWindow(this);
			setName(((Screen)content).getDefinition().getName());
		}	
	}

	/**
	 * Sets the text in the name label of the screen
	 */
	public void setName(String nme) {
		name.setText(nme);
	}

	/**
	 * Stub method implemented to satisfy interface
	 */
	public void close() {
		// Do Nothing
	}

	/**
	 * Stub method implemented to satisfy interface
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * Stub method implemented to satisfy interface
	 */
	public void setMessagePopup(ArrayList<LocalizedException> exceptions,
			String style) {
	}

	/**
	 * Stub method implemented to satisfy interface
	 */
	public void clearMessagePopup(String style) {
	}

	/**
	 * Stub method implemented to satisfy interface
	 */
	public void setStatus(String text, String style) {
	}

	/**
	 * Lays a glass panel of the content of the window to disallow user interaction
	 */
	public void lockWindow() {
    	if(glass == null) {
    		glass = new AbsolutePanel();
    		glass.setStyleName("GlassPanel");
    		glass.setHeight(content.getOffsetHeight()+"px");
    		glass.setWidth(content.getOffsetWidth()+"px");
    		RootPanel.get().add(glass, content.getAbsoluteLeft(),content.getAbsoluteTop());
    	}
	}

	/**
	 * Removes the glass panel to allow the user to interact with the screen.
	 */
	public void unlockWindow() {
    	if(glass != null) {
    		glass.removeFromParent();
    		glass = null;
    	}
	}

	/**
	 * Pops up a busy confirmation window with only a spinning icon and no text
	 */
	public void setBusy() {
		setBusy("");
	}

	/**
	 * Pops up a busy confirmation window with a spinngin icon and and the passed message
	 */
	public void setBusy(String message) {
		confirm = new Confirm(Confirm.Type.BUSY,null,message,null);
		confirm.show(-1,80);
	}

	/**
	 * Hides the confirmation dialog if showing
	 */
	public void clearStatus() {
		if(confirm != null)
			confirm.hide();
	}

	/**
	 * Hides the confirmation dialog if showing
	 */
	public void setDone(String message) {
		clearStatus();
	}

	/**
	 * Pops up an Error confirmation with the passed message
	 */
	public void setError(String message) {
		confirm = new Confirm(Confirm.Type.ERROR,"Error",message,"OK");
		confirm.show(-1,80);
	}
	
	/**
	 * Stub method implemented to satisfy interface
	 */
	public void setProgress(int percent) {

	}

	@Override
	public HandlerRegistration addCloseHandler(CloseHandler<WindowInt> handler) {
		// TODO Auto-generated method stub
		return null;
	}
}
