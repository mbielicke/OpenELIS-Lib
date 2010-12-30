/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.persistence;

import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.openelis.util.SessionManager;

/**
 * Class is used as a convenience method for looking up a remote bean. The
 * initial context is retrieved from the session (see HostedFilter &
 * StaticFilter).
 */
public class EJBFactory {
    private static Logger log = Logger.getLogger(EJBFactory.class.getName());

    public static Object lookup(String bean) {
        InitialContext c;
        Properties p = null;

        try {
            p = (Properties)SessionManager.getSession().getAttribute("jndiProps");
            if (p == null) {
                log.error("Failed to get user properties for thread id " + Thread.currentThread());
            } else {
                c = new InitialContext(p);
                if (c == null)
                    log.error("Failed to get the initial context for thread id "+ Thread.currentThread());
                else {
                    return c.lookup(bean);
                }
            }
        } catch (Exception e) {
            log.error("Failed to lookup "+ bean +" for thread id "+ Thread.currentThread()+": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
