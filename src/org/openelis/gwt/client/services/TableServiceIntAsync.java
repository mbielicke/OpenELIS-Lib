package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.TableModel;

/**
 * TableServiceIntAsync is the Asynchronous version of
 * the TableServiceInt interface.
 * @author tschmidt
 *
 */
public interface TableServiceIntAsync {
    public void getPage(int page, int selected, AsyncCallback callback) throws RPCException;

    public void sort(int col,
                     boolean down,
                     int index,
                     int selected,
                     AsyncCallback callback) throws RPCException;

    public void filter(int col,
                       Filter[] filters,
                       int index,
                       int selected,
                       AsyncCallback callback) throws RPCException;

    public void getFilter(int col, AsyncCallback callback);

    public void getModel(TableModel model, AsyncCallback callback);

    public void saveModel(TableModel model, AsyncCallback callback);
    
    public void getTip(AbstractField key, AsyncCallback callback);
}
