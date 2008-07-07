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
import org.openelis.util.ValidationException;
import org.apache.log4j.Category;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract parent class for all the action classes in inmsp project. This class
 * applies the Command design pattern and Template design pattern.
 * 
 * @author fyu
 */
public abstract class AbstractAction implements IAction {
    protected static Category log = Category.getInstance(AbstractAction.class.getName());

    /**
     * Constructor.
     * 
     * @see java.lang.Object#Object()
     */
    public AbstractAction() {
    }

    /**
     * Execute user request. This method contains work flow algothrim for
     * action. Subclass needs to provide the implementation if needed.
     * 
     * @param inmspRequest
     * @throws IOException
     */
    public abstract void execute(FormRPC form) throws SQLException,
                                              IOException,
                                              ValidationException,
                                              RPCException;

    public void execute(HttpServletRequest req, HttpServletResponse resp) throws SQLException,
                                                                         IOException,
                                                                         ValidationException {
    }

    /**
     * Perform update.
     * 
     * @param inmspRequest
     */
    public abstract boolean hasPermission(FormRPC form) throws SQLException,
                                                       IOException,
                                                       ValidationException;
}
