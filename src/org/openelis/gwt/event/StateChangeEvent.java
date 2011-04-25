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

import com.google.gwt.event.shared.GwtEvent;

/**
 * This class will fire events to registered objects for StateChange actions
 *
 * @param <I>
 */
public class StateChangeEvent<I> extends GwtEvent<StateChangeHandler<I>>{
	
	private static Type<StateChangeHandler<?>> TYPE;
	private I state;
	
	@SuppressWarnings("rawtypes")
    public static <I> void fire(HasStateChangeHandlers source, I state) {
	    if (TYPE != null) {
	      StateChangeEvent<I> event = new StateChangeEvent<I>(state);
	      source.fireEvent(event);
	    }
    }

    protected StateChangeEvent(I state) {
    	this.state = state;
    }
    
	@Override
	protected void dispatch(StateChangeHandler<I> handler) {
		handler.onStateChange(this);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final Type<StateChangeHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<StateChangeHandler<?>> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<StateChangeHandler<?>>();
	    }
	    return TYPE;
	 }
	
	/**
	 * Method returns the new State for the source object
	 * @return
	 */
	public I getState() {
		return state;
	}
}
