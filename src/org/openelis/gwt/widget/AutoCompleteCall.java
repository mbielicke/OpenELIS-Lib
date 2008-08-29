package org.openelis.gwt.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.services.AutoCompleteServiceInt;
import org.openelis.gwt.services.AutoCompleteServiceIntAsync;

import java.util.HashMap;

public class AutoCompleteCall {
    
    private AutoCompleteServiceIntAsync autoService = (AutoCompleteServiceIntAsync) GWT
    .create(AutoCompleteServiceInt.class);

    private ServiceDefTarget target = (ServiceDefTarget) autoService;
    
    private AutoCompleteDropdown widget; 
    
    private AsyncCallback callback = new AsyncCallback() {
        public void onSuccess(Object result) {
            widget.showAutoMatches((DataModel)result);
        }
        
        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }
    };
    
    public AutoCompleteCall(String url, AutoCompleteDropdown widget) {
        this.widget = widget;
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }

    public void callForMatches(String cat, DataModel model, String text, HashMap params) {
        try {
            autoService.getMatches(cat, model, text, params, callback);
        } catch (RPCException e) {
            Window.alert(e.getMessage());
        }
    }

}
