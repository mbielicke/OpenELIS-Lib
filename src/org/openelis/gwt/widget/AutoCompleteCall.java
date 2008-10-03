package org.openelis.gwt.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModel;
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

    
    public void callForMatches(final AutoComplete widget, DataModel model, String text) {
        try {
            autoService.getMatches(widget.cat, model, text, null, new AsyncCallback() {
                public void onSuccess(Object result) {
                    widget.showAutoMatches((DataModel)result);
                }
                
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }
            });
        } catch (RPCException e) {
            Window.alert(e.getMessage());
        }
    }

}
