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

import java.util.HashMap;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.openelis.gwt.server.ScreenControllerServlet;
import org.openelis.util.SessionManager;

/**
 * Class is used as a convenience method for looking up a remote bean. The
 * initial context is retrieved from the session (see HostedFilter &
 * StaticFilter).
 */
public class EJBFactory {
    private static Logger log = Logger.getLogger(EJBFactory.class.getName());

    /**
     * This class provides a caching EJB bean factory. All cached beans are stored in
     * user's session and are removed when the session expires.
     * 
     * Note that this method caches a single instance of each bean. Returned beans
     * are marked as busy until the servlet thread is complete. If another thread
     * for the same user requests for a bean that is busy, ie. used in another thread,
     * a new bean with initial context will be created and returned. Subsequent bean will
     * not be cached.
     * 
     * @see ScreenControllerServlet for how the busy pool is cleared. 
     */
    public static Object lookup(String bean) {
        boolean isBusy;
        Object object;
        InitialContext c;
        Properties p = null;
        HashMap<String, Object> availPool, busyPool; 

//        availPool = (HashMap) SessionManager.getSession().getAttribute("availableBeanPool");
//        busyPool = (HashMap) SessionManager.getSession().getAttribute("busyBeanPool");
        
//        if (availPool == null || busyPool == null) {
//            availPool = new HashMap<String, Object>();
//            busyPool = new HashMap<String, Object>();
//            SessionManager.getSession().setAttribute("availableBeanPool", availPool);
//            SessionManager.getSession().setAttribute("busyBeanPool", busyPool);
//        }

//        object = (Object) availPool.get(bean);
//        isBusy = busyPool.containsKey(bean);
//        if (object == null || isBusy) {
            try {
                p = (Properties)SessionManager.getSession().getAttribute("jndiProps");
                if (p == null) {
                    log.error("Failed to get user properties for thread id " + Thread.currentThread());
                    return null;
                }

                c = new InitialContext(p);
                object = c.lookup(bean);
            } catch (Exception e) {
                log.error("Failed to lookup "+ bean +" for thread id "+ Thread.currentThread()+": " +
                          e.getMessage());
                e.printStackTrace();
                object = null;
            }
//            if (! isBusy) {
//                availPool.put(bean, object);
//                busyPool.put(bean, object);
//            }
//        }

        return object;
    }
}
