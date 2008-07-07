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
package org.openelis.interfaces;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.util.ValidationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * The parent class for all Action subclass.
 * 
 * @author fyu
 */
public interface IAction {
    public void execute(FormRPC form) throws SQLException,
                                     IOException,
                                     ValidationException,
                                     RPCException;

    public boolean hasPermission(FormRPC form) throws SQLException,
                                              IOException,
                                              ValidationException;

    public AbstractField query(FormRPC form) throws SQLException,
                                     IOException,
                                     ValidationException,
                                     RPCException;
}
