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

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;

import java.util.HashMap;

/**
 * AutoCompleteServiceIntAsync is the Asynchronous version of
 * the AutoCompleteServiceInt interface.
 * @author tschmidt
 *
 */
public interface AutoCompleteServiceIntAsync {

    public void getMatches(String cat, DataModel model, String match, HashMap<String,DataObject> params, AsyncCallback callback) throws RPCException;

}
