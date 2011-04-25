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
 * This class will fire events to registered objects for DragStart actions. 
 * @param <I>
 */
public class DragStartEvent<I> extends GwtEvent<DragStartHandler<I>> {
	
	private static Type<DragStartHandler<?>> TYPE;
	private I dragObject;
	
	public static <I> DragStartEvent<I> fire(HasDragStartHandlers<I> source, I dragObject) {
		if(TYPE != null) {
			DragStartEvent<I> event = new DragStartEvent<I>(dragObject);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected DragStartEvent(I dragObject) {
		this.dragObject = dragObject;
	}
	
	protected void dispatch(DragStartHandler<I> handler) {
		handler.onDragStart(this);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Type<DragStartHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<DragStartHandler<?>> getType() {
	   if (TYPE == null) {
		   TYPE = new Type<DragStartHandler<?>>();
	   }
	   return TYPE;
	}
	
	/**
	 * Method returns the object being dragged
	 * @return
	 */
	public I getDragObject() {
		return dragObject;
	}

}
