/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.TableModel;

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
    
    public String getTip(AbstractField key) throws RPCException;
    
}
