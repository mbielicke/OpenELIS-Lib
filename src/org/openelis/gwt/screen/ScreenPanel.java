/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

/**
 *  This class holds all widgets for a screen into a single panel and has logic to handle
 *  set focus to widgets and well as keyboard shortcuts to widgets.
 */
public class ScreenPanel extends AbsolutePanel implements HasClickHandlers, FocusHandler, HasFocusHandlers {
	
	/**
	 * The currently focused widget
	 */
	protected Widget focused;
	
	/**
	 * No-Arg constructor
	 */
	public ScreenPanel() {

	}

	/**
	 * Method returns the currently focused widget in the panel
	 * @return
	 */
	public Widget getFocused() {
		return focused;
	}

	/**
	 * Method is called when a widget in the panel receives focus.  Focused is set to 
	 * the new widget and then a Event is fired to all listening widgets that the focus
	 * of the panel has changed
	 */
	public void onFocus(FocusEvent event) {
		if(event.getSource() != null){
			focused = (Widget)event.getSource();
		}
		FocusEvent.fireNativeEvent(event.getNativeEvent(), this);
	}
	
	/**
	 * Sets the foucs of the panel to the widget passed.  If null is passed then 
	 * focus will be removed from all widgets
	 */
	public void setFocusWidget(final Widget widget) {
		focused = widget;
		if(widget instanceof Focusable) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				public void execute() {
					((Focusable)(widget)).setFocus(true);
				}
			});
		}
		FocusEvent.fireNativeEvent(Document.get().createFocusEvent(), this);
	}
	
	/**
	 * Registers a FocusHandler to this panel
	 */
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addHandler(handler,FocusEvent.getType());
	}
	
	/**
	 * Adds a new ClickHandler to the panel
	 */
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

}

