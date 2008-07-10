/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.xml.client.Node;

/** 
 * ScreenToggleButton wraps a GWT ToggleButtton to be displayed on a Screen 
 * @author tschmidt
 *
 */
public class ScreenToggleButton extends ScreenWidget {
	/**
	 * Default Tag Name for XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "toggle";
	/**
	 * Widget wrapped by this class
	 */
	private ToggleButton button;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
	public ScreenToggleButton() {
	}

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;toggle key="string" html="escaped HTML" text="string" constant = "boolean"/&gt;
     * @param node
     * @param screen
     */
	public ScreenToggleButton(Node node, final ScreenBase screen) {
		super(node);
        final ScreenToggleButton st = this;
		button = new ToggleButton() {
			public void onBrowserEvent(Event event) {
				if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
					if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
						screen.doTab(event, st);
					}
				} else {
					super.onBrowserEvent(event);
				}
			}
		};
		button.setStyleName("ScreenToggleButton");
		button.addClickListener((ClickListener)screen);
		if(node.getAttributes().getNamedItem("text") != null){
		    button.setText(node.getAttributes().getNamedItem("text")
						.getNodeValue());
		}
		if (node.getAttributes().getNamedItem("html") != null)
			button.setHTML(node.getAttributes().getNamedItem("html")
					.getNodeValue());
		initWidget(button);
		setDefaults(node, screen);
	}

	public ScreenWidget getInstance(Node node, ScreenBase screen) {
		return new ScreenToggleButton(node, screen);
	}

	public void setFocus(boolean focus) {

	}
    
    public void destroy() {
        button = null;
        super.destroy();
    }
}
