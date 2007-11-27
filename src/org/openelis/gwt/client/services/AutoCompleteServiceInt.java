package org.openelis.gwt.client.services;

import org.openelis.gwt.common.AutoCompleteRPC;

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

    public AutoCompleteRPC getMatches(String cat, String match);

    public AutoCompleteRPC getDisplay(String cat, Integer value);

}
