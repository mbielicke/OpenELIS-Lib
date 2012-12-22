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

import com.google.gwt.user.client.ui.Widget;

/**
 * This class sets up a listener for a combination of keys to use a shortcut to widgets on a screen
 *
 */
public class Shortcut {

	/**
	 * Flag if ctrl key is part of combination
	 */
	protected boolean ctrl;
	/**
	 * Flag if shift key is part of combination
	 */
	protected boolean shift;
	/**
	 * Flag if alt key is part of combination
	 */
	protected boolean alt;
	/**
	 * Key used in combiniation
	 */
	protected char key;
	/**
	 * Widget used for the shortcut
	 */
	protected Widget wid;
	
	/**
	 * Constructor
	 * @param ctrl
	 * @param shift
	 * @param alt
	 * @param key
	 * @param wid
	 */
	public Shortcut(boolean ctrl,boolean shift,boolean alt,char key,Widget wid) {
		this.ctrl = ctrl;
		this.shift = shift;
		this.alt = alt;
		this.key = Character.toUpperCase(key);
		this.wid = wid;
	}
	
	public Shortcut(boolean ctrl,boolean shift,boolean alt,char key) {
		this(ctrl,shift,alt,key,null);
	}
	
	@Override
	public boolean equals(Object obj) {
		Shortcut comp;
		
		if(!(obj instanceof Shortcut))
			return super.equals(obj);
		
		comp = (Shortcut)obj;
		
		return comp.ctrl  == ctrl  &&
		       comp.alt   == alt   &&
		       comp.shift == shift && 
		       comp.key   == key;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 31 * result + (ctrl  ? 1 : 0);
		result = 31 * result + (shift ? 1 : 0);
		result = 31 * result + (alt   ? 1 : 0);
		result = 31 * result + key;
		
		return result; 
	}
	
}