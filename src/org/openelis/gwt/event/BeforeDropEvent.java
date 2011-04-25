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
 * This class will fire an Event to registered objects before a Drop actions occurs.
 * The handler can cancel the drop calling event.cancel()
 *
 * @param <I>
 */
public class BeforeDropEvent<I> extends GwtEvent<BeforeDropHandler<I>> {
	
	private static Type<BeforeDropHandler<?>> TYPE;
	private I dragObject;
	private Object dropTarget;
	private boolean cancelled;
	
	public static <I> BeforeDropEvent<I> fire(HasBeforeDropHandlers<I> source, I dragObject, Object dropTarget) {
		if(TYPE != null) {
			BeforeDropEvent<I> event = new BeforeDropEvent<I>(dragObject, dropTarget);
			//source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	public BeforeDropEvent(I dragObject, Object dropTarget) {
		this.dragObject = dragObject;
		this.dropTarget = dropTarget;
	}

	@Override
	protected void dispatch(BeforeDropHandler<I> handler) {
		handler.onBeforeDrop(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeDropHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeDropHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<BeforeDropHandler<?>>();
		}
		return TYPE;
	}

	/**
	 * Method will return the object being dragged.
	 * @return
	 */
	public I getDragObject() {
		return dragObject;
	}
	
	/**
	 * Method will return the proposed object that is being dropped on.
	 * @return
	 */
	public Object getDropTarget() {
		return dropTarget;
	}

	/**
	 * Method can be called to cancel the Drop action by the handler
	 */
	public void cancel() {
		cancelled = true;
	}

	/**
	 * Method used to determine if the event has been canceled by a handler.
	 * @return
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	
}
