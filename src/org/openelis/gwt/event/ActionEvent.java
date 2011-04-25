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
 * This class is used fire Logical Actions between components.
 *
 * @param <I>
 */
public class ActionEvent<I> extends GwtEvent<ActionHandler<I>>{
	
	private static Type<ActionHandler<?>> TYPE;
	private I action;
	private Object data;
	private boolean failed;
	
    public static <I> ActionEvent<I> fire(HasActionHandlers<I> source, I action, Object data) {
	    if (TYPE != null) {
	      ActionEvent<I> event = new ActionEvent<I>(action,data);
	      source.fireEvent(event);
	      return event;
	    }
	    return null;
    }

    protected ActionEvent(I state,Object data) {
    	this.action = state;
    	this.data = data;
    }
    
	@Override
	protected void dispatch(ActionHandler<I> handler) {
		handler.onAction(this);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final Type<ActionHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<ActionHandler<?>> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<ActionHandler<?>>();
	    }
	    return TYPE;
	 }
	
	/**
	 * Returns the Action enum for this event.
	 * @return
	 */
	public I getAction() {
		return action;
	} 
	
	/**
	 * Returns any Data object that may have been attached to this event.
	 * @return
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * Sets a flag letting the firing object know that the Action failed in the 
	 * handling code and recover appropriately
	 */
	public void fail() {
		failed = true;
	}
	
	/**
	 * Method used to determine if the handling of this Action failed.
	 * @return
	 */
	public boolean failed() {
		return failed;
	}
}
