package org.openelis.gwt.services;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * AutoCompleteServiceIntAsync is the Asynchronous version of
 * the AutoCompleteServiceInt interface.
 * @author tschmidt
 *
 */
public interface AutoCompleteServiceIntAsync {

    public void getMatches(String cat, DataModel model, String match, HashMap params, AsyncCallback callback) throws RPCException;

}
