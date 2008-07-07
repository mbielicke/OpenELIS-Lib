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

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;

/**
 * ScreenServiceInt is a GWT RemoteService interface for the Screen
 * Widget.  GWT RemoteServiceServlets that want to provide server
 * side logic for Screens must implement this interface.
 * 
 * @author tschmidt
 *
 */
public interface ScreenServiceInt extends RemoteService {

    public FormRPC action(FormRPC rpc) throws RPCException;
    
    public AbstractField query(FormRPC rpc) throws RPCException;
    
}
