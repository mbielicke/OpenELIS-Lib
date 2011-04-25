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
 * This class will fire BeforeGetMatchesEvent to registered objects.  
 * The handler can cancel the event by calling event.cancel().
 *
 */
public class BeforeGetMatchesEvent extends GwtEvent<BeforeGetMatchesHandler> {
	
	private static Type<BeforeGetMatchesHandler> TYPE;
	private String match;
	private boolean cancelled;
	
	public static BeforeGetMatchesEvent fire(HasBeforeGetMatchesHandlers source, String match) {
		if(TYPE != null) {
			BeforeGetMatchesEvent event = new BeforeGetMatchesEvent(match);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeGetMatchesEvent(String match) {
		this.match = match;
	}

	@Override
	protected void dispatch(BeforeGetMatchesHandler handler) {
		handler.onBeforeGetMatches(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeGetMatchesHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeGetMatchesHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeGetMatchesHandler>();
		}
		return TYPE;
	}
	
	/**
	 * Returns the typed entry by the user that is to be matched.
	 * @return
	 */
	public String getMatch() {
		return match;
	}
	
	/**
	 * Method used to cancel the event by the handler
	 */
	public void cancel() {
		cancelled = true;
	}
	
	/**
	 * Method used to determine if the event has been canceled.
	 * @return
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	

}
