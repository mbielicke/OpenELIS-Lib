package org.openelis.gwt.client.services;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * AutoCompleteServiceIntAsync is the Asynchronous version of
 * the AutoCompleteServiceInt interface.
 * @author tschmidt
 *
 */
public interface AutoCompleteServiceIntAsync {

    public void getMatches(String cat, DataModel model, String match, AsyncCallback callback);

    public void getDisplay(String cat, DataModel model, AbstractField value, AsyncCallback callback);
    
   // public void getInitialModel(String cat, AsyncCallback callback);
}
