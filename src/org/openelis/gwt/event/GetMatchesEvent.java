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
 * This class will fire events to objects registered to receive Get Matches action
 * @author tschmidt
 *
 */
public class GetMatchesEvent extends GwtEvent<GetMatchesHandler>{
	
	private static Type<GetMatchesHandler> TYPE;
	private String match;
	private boolean failed;
	
    public static GetMatchesEvent fire(HasGetMatchesHandlers source, String match) {
	    if (TYPE != null) {
	      GetMatchesEvent event = new GetMatchesEvent(match);
	      source.fireEvent(event);
	      return event;
	    }
	    return null;
    }

    protected GetMatchesEvent(String match) {
    	this.match = match;
    }
    
	@Override
	protected void dispatch(GetMatchesHandler handler) {
		handler.onGetMatches(this);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public final Type<GetMatchesHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<GetMatchesHandler> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<GetMatchesHandler>();
	    }
	    return TYPE;
	 }
	
	/**
	 * Method will return the typed entry by the user to be matched
	 * @return
	 */
	public String getMatch() {
		return match;
	} 
		
	/**
	 * Method called to notify source that the action failed
	 */
	public void fail() {
		failed = true;
	}
	
	/**
	 * Method used to determine if the event was marked as failed by a handler
	 * @return
	 */
	public boolean failed() {
		return failed;
	}
}
