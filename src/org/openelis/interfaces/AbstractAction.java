/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.interfaces;

import org.apache.log4j.Category;
import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.ValidationException;

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
@Deprecated public abstract class AbstractAction implements IAction {
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
    public abstract void execute(Form form) throws SQLException,
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
    public abstract boolean hasPermission(Form form) throws SQLException,
                                                       IOException,
                                                       ValidationException;
}
