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
 * This class will fire an Event to registered objects before a Close event is to occur.  
 * The action can be canceled by calling event.cancel() in the handler.  We implemented this
 * event because is was supplied with GWT.
 *
 * @param <T>
 */
public class BeforeCloseEvent<T> extends GwtEvent<BeforeCloseHandler<T>> {
	
	private static Type<BeforeCloseHandler<?>> TYPE;
	private T target;
	private boolean cancelled;
	
	public static <T> BeforeCloseEvent<T> fire(HasBeforeCloseHandlers<T> source, T target) {
		if(TYPE != null) {
			BeforeCloseEvent<T> event = new BeforeCloseEvent<T>(target);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeCloseEvent(T target) {
		this.target = target;
	}

	@Override
	protected void dispatch(BeforeCloseHandler<T> handler) {
		handler.onBeforeClosed(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeCloseHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeCloseHandler<?>> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeCloseHandler<?>>();
		}
		return TYPE;
	}
	
	public T getTarget() {
		return target;
	}
		
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
