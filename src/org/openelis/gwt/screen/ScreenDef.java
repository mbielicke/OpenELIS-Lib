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

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.ui.Widget;

/**
 *  This is a default implementation of the ScreenDefInt.  It can be used to create a screen
 *  from code that does not have generated definition from XML and the UIGenerator.
 */
public class ScreenDef implements ScreenDefInt {
	
	/**
	 * panel used to Hold the widgets
	 */
	protected ScreenPanel panel;
	/**
	 * HashMap providing reference to all widgets by key
	 */
	protected HashMap<String,Widget> widgets;
	/**
	 * Name of the screen to display in Window Caption
	 */
	protected String name;

	/**
	 * No-Arg constructor
	 */
	public ScreenDef() {
		widgets = new HashMap<String,Widget>();
		panel = new ScreenPanel();
	}
	
	/**
	 * Replaces the widgets HashMap with the one passed into the method
	 */
	public void setWidgets(HashMap<String,Widget> widgets) {
		this.widgets = widgets;
	}
	
	/**
	 * Returns the HashMap of widget references
	 */
	public HashMap<String,Widget> getWidgets() {
		return widgets;
	}
	
	/**
	 * Returns the widget referenced by the passed key value
	 */
	public Widget getWidget(String key) {
		return widgets.get(key);
	}
	
	/**
	 * Sets a reference of the widget into the HashMap of widgets by the key passed
	 */
	public void setWidget(Widget widget, String key) {
		widgets.put(key, widget);
	}
	
	/**
	 * Returns the ScreenPanel for this definition that contains the laid out widgets.
	 */
	public ScreenPanel getPanel() {
		return panel;
	}
	
	/**
	 * Returns the name used by this screen.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name for this screen to use
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ArrayList<Shortcut> getShortcuts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Widget, Tab> getTabs() {
		// TODO Auto-generated method stub
		return null;
	}
}
