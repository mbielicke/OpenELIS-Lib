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
package org.openelis.persistence;

import org.openelis.util.SessionManager;

import java.util.Properties;

import javax.naming.InitialContext;

public class EJBFactory {

    public static Object lookup(String bean){
        try{
            Properties props = (Properties)SessionManager.getSession().getAttribute("jndiProps");
            InitialContext ctx = null;
            ctx = new InitialContext(props);
            return ctx.lookup(bean);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
