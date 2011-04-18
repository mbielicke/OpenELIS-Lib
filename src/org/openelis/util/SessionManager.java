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
package org.openelis.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionManager implements HttpSessionListener {
    protected static HashMap<String, HttpSession> sessions;
    protected static HashMap<String, String> threads;
    
    static {
        sessions = new HashMap<String, HttpSession>();
        threads = new HashMap<String, String>();
    }
    
    public static HttpSession getSession() {
        String tName;
        
        tName = Thread.currentThread().getName();
        return (HttpSession)sessions.get((String)threads.get(tName));
    }
    
    public static void setSession(HttpSession session) {
        sessions.put(session.getId(), session);
        threads.put(Thread.currentThread().getName(), session.getId());
    }

    public static void removeSession(String Id) {
        Map.Entry entry;
        Iterator thread;
        
        try {
            if (sessions.remove(Id) != null) {
                thread = threads.entrySet().iterator();
                while (thread.hasNext()) {
                    entry = (Map.Entry)thread.next();
                    if (entry.getValue().equals(Id)) {
                        threads.remove(entry.getKey());
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    //
    // implemented to catch session expiration
    //
    public void sessionCreated(HttpSessionEvent arg0) {
    }

    public void sessionDestroyed(HttpSessionEvent arg0) {
        removeSession(arg0.getSession().getId());
    }
}
