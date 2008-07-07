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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.services;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;

public interface AppScreenFormServiceInt extends AppScreenServiceInt {
    
    public FormRPC commitUpdate(FormRPC rpcSend, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC commitAdd(FormRPC rpcSend, FormRPC rpcReturn) throws RPCException;
    
    public DataModel commitQuery(FormRPC rpcSend, DataModel model) throws RPCException;
    
    public FormRPC fetch(DataSet key, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC fetchForUpdate(DataSet key, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC commitDelete(DataSet key, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC abort(DataSet key, FormRPC rpcReturn) throws RPCException;
}
