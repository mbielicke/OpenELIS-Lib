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

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataObject;

import java.util.HashMap;

public interface AppScreenServiceInt extends RemoteService {
    
    public String getXML() throws RPCException;
    
    public HashMap<String,DataObject> getXMLData() throws RPCException;
    
    public HashMap<String,DataObject> getXMLData(HashMap<String,DataObject> args) throws RPCException;

}
