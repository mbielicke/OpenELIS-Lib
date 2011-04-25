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
package org.openelis.gwt.event;

import org.openelis.gwt.screen.ScreenEventHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class will fire events to registered objects for a DataChange action. If a target is passed
 * to the fire() method than only the handler for that widget will be called and all other handlers
 * will be ignored. 
 *
 */
public class DataChangeEvent extends GwtEvent<DataChangeHandler>{
	
	private static Type<DataChangeHandler> TYPE;
	private Widget target;
	
	/**
	 * Passing a target to this methd will fire only the handler registered to that
	 * widget and no other handlers will be called.
	 * @param source
	 * @param target
	 */
	public static void fire(HasDataChangeHandlers source, Widget target) {
	    if (TYPE != null) {
		    DataChangeEvent event = new DataChangeEvent(target);
		    source.fireEvent(event);
		}
	}
	
    public static void fire(HasDataChangeHandlers source) {
	    if (TYPE != null) {
	      DataChangeEvent event = new DataChangeEvent(null);
	      source.fireEvent(event);
	    }
    }
    
    protected DataChangeEvent(Widget target) {
    	this.target = target;
    }

	@SuppressWarnings("rawtypes")
	@Override
	protected void dispatch(DataChangeHandler handler) {
		if(target == null || (handler instanceof ScreenEventHandler && target == ((ScreenEventHandler)handler).target))
			handler.onDataChange(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Type<DataChangeHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<DataChangeHandler> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<DataChangeHandler>();
	    }
	    return TYPE;
	 }
	
	
}
