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
package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.rewrite.data.TableDataRow;
import org.openelis.gwt.services.AutoCompleteServiceInt;
import org.openelis.gwt.services.AutoCompleteServiceIntAsync;

public class AutoCompleteCall implements AutoCompleteCallInt {
    
    private AutoCompleteServiceIntAsync autoService = (AutoCompleteServiceIntAsync) GWT
    .create(AutoCompleteServiceInt.class);

    private ServiceDefTarget target = (ServiceDefTarget) autoService;
    
    public AutoCompleteCall(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }

    
    public void callForMatches(final AutoComplete widget, String text) {
    	/*
        try {
            autoService.getMatches(widget.cat, model, text, null, new AsyncCallback<ArrayList<>>() {
                public void onSuccess(Object result) {
                    widget.showAutoMatches((TableDataModel)result);
                }
                
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }
            });
            
        } catch (RPCException e) {
            Window.alert(e.getMessage());
        }
        */
    }

}
