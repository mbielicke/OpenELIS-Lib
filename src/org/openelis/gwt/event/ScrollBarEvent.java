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
 *  This class will fire events to registered objects for ScrollBar actions 
 *
 */
public class ScrollBarEvent extends GwtEvent<ScrollBarHandler>{
    
    private static Type<ScrollBarHandler> TYPE;
    int pos;
    
    public static void fire(HasScrollBarHandlers source,int pos) {
        if (TYPE != null) {
            ScrollBarEvent event = new ScrollBarEvent(pos);
            source.fireEvent(event);
        }
    }
    
    private ScrollBarEvent(int pos) {
    	this.pos = pos;
    }

    @Override
    protected void dispatch(ScrollBarHandler handler) {
        handler.onScroll(this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Type<ScrollBarHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    public static Type<ScrollBarHandler> getType() {
       if (TYPE == null) {
          TYPE = new Type<ScrollBarHandler>();
        }
        return TYPE;
    }
    
    public int getPosition() {
    	return pos;
    }
    
}
