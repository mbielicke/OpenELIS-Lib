package org.openelis.gwt.client.services;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;

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

    public DataModel getMatches(String cat, DataModel model, String match);

    public DataModel getDisplay(String cat, DataModel model, AbstractField value);
    
   // public DataModel getInitialModel(String cat);

}
