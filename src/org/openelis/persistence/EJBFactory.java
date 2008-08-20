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
