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

/**
 * This class represents the tab sequence for a widget on a screen
 * @author tschmidt
 *
 */
public class Tab {
	/**
	 * Key for the widget this tab is for
	 */
	protected String widget;
	/**
	 * Key for the next widget in the tab sequence
	 */
	protected String next;
	/**
	 * Key for the previous widget in the tab sequence
	 */
	protected String prev;

	/**
	 * Constructor
	 * @param widget
	 * @param next
	 * @param prev
	 */
	public Tab(String widget, String next, String prev) {
		this.widget = widget;
		this.next = next;
		this.prev = prev;
	}

	/**
	 * Method sets the key for the widget this tab handles
	 * @param widget
	 */
	public void setWidget(String widget) {
		this.widget = widget;
	}
	
	/**
	 * Method returns the key for the widget this tab handles
	 * @return
	 */
	public String getWidget() {
		return widget;
	}

	/**
	 * Returns the key for the next widget in the tab sequence
	 * @return
	 */
	public String getNext() {
		return next;
	}

	/**
	 * Sets the key for the next widget in the tab sequence
	 * @param next
	 */
	public void setNext(String next) {
		this.next = next;
	}

	/**
	 * Returns the key for the previous widget in the tab sequence
	 * @return
	 */
	public String getPrev() {
		return prev;
	}

	/**
	 * Sets the key for the previous widget in the tab sequence
	 * @param prev
	 */
	public void setPrev(String prev) {
		this.prev = prev;
	}
}
