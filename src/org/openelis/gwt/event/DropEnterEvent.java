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
 * This class will fire events to registered objects for DropEnter actions.  
 * Handlers can let the DropController know that this is not a valid drop area by calling
 * event.cancel()
 * @author tschmidt
 *
 * @param <I>
 */
public class DropEnterEvent<I> extends GwtEvent<DropEnterHandler<I>> {
	
	private static Type<DropEnterHandler<?>> TYPE;
	private I dragObject;
	private Object dropTarget;
	private boolean cancelled;
	public enum DropPosition {BELOW, ON, ABOVE};
	public DropPosition dropPos;
	
	public static <I> DropEnterEvent<I> fire(HasDropEnterHandlers<I> source, I dragObject, Object dropTarget, DropPosition pos) {
		if(TYPE != null) {
			DropEnterEvent<I> event = new DropEnterEvent<I>(dragObject, dropTarget, pos);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	public DropEnterEvent(I dragObject, Object dropTarget, DropPosition pos) {
		this.dragObject = dragObject;
		this.dropTarget = dropTarget;
		this.dropPos = pos;
	}

	@Override
	protected void dispatch(DropEnterHandler<I> handler) {
		handler.onDropEnter(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DropEnterHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<DropEnterHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<DropEnterHandler<?>>();
		}
		return TYPE;
	}

	/**
	 * Method returns the object being dragged and proposed to be dropped
	 * @return
	 */
	public I getDragObject() {
		return dragObject;
	}
	
	/**
	 * Method returns the object is currently hovered on and proposed to be 
	 * dropped on.
	 * @return
	 */
	public Object getDropTarget() {
		return dropTarget;
	}

	/**
	 * Method will not allow the drop to occur here and the Drop Indicator will change to
	 * show a drop is not allowed here. 
	 */
	public void cancel() {
		cancelled = true;
	}

	/**
	 * Method used to determine if a this is no longer a valid drop canceled by the handler
	 * @return
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Returns the enum of the DropPosition being either ABOVE, ON, or BELOW the current drop target
	 * @return
	 */
	public DropPosition getDropPosition() {
		return dropPos;
	}
	
}
