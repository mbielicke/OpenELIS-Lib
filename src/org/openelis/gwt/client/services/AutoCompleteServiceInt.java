package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * AutoCompleteServiceInt is a GWT RemoteService interface to be
 * implemented by GWT RemoteServiceServlets for the AutoComplete 
 * widget to make calls to the server for the matching options 
 * for the text that the users have entered. 
 * 
 * @author tschmidt
 *
 */
public interface AutoCompleteServiceInt extends RemoteService {

    public void getMatches(String cat, String match);

    public void getDisplay(String cat, Integer value);

}
