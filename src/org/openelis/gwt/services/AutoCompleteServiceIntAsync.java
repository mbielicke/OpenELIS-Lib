package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;

import java.util.HashMap;

/**
 * AutoCompleteServiceIntAsync is the Asynchronous version of
 * the AutoCompleteServiceInt interface.
 * @author tschmidt
 *
 */
public interface AutoCompleteServiceIntAsync {

    public void getMatches(String cat, DataModel model, String match, HashMap<String,DataObject> params, AsyncCallback callback) throws RPCException;

}
