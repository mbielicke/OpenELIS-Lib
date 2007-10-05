package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.TableModel;

/**
 * TableServiceInt is a GWT RemoteService interface that provides 
 * server side methods for the Table widget.  A GWT RemoteServiceServlet
 * wanting to provide server side logic for a Table widget must implement
 * this interface.
 * 
 * @author tschmidt
 * 
 */
public interface TableServiceInt extends RemoteService {
    public TableModel getPage(int page, int selected) throws RPCException;

    public TableModel sort(int col, boolean down, int index, int selected) throws RPCException;

    public TableModel filter(int col, Filter[] filters, int index, int selected) throws RPCException;

    public Filter[] getFilter(int col);

    public TableModel getModel(TableModel model) throws RPCException;

    public TableModel saveModel(TableModel model) throws RPCException;
}
