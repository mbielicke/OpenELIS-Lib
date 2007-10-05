package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * AutoCompleteServiceIntAsync is the Asynchronous version of
 * the AutoCompleteServiceInt interface.
 * @author tschmidt
 *
 */
public interface AutoCompleteServiceIntAsync {

    public void getMatches(String cat, String match, AsyncCallback callback);

    public void getDisplay(String cat, Integer value, AsyncCallback callback);
}
