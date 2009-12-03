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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Category;

import edu.uiowa.uhl.wa.bean.ApplicationLogBuilder;

public class SessionManager implements HttpSessionListener, HttpSessionAttributeListener{
    private static Category log = Category.getInstance(SessionManager.class.getName());
    private static Hashtable sessions = new Hashtable();
    private static HashMap threads = new HashMap();
    private static String appName ="Not Set"; 

    public static HttpSession getSession() {
        String tName = Thread.currentThread().getName();
        if (threads.containsKey(tName)) {
            return (HttpSession)sessions.get((String)threads.get(tName));
        } else {
            return null;
        }
    }

    public static void setSession(HttpSession session) {
        sessions.put(session.getId(), session);
        threads.put(Thread.currentThread().getName(), session.getId());
    }

    public static void removeSession(String Id) {
        try {
            log.debug("removing session " + Id);
            sessions.remove(Id);
            Iterator threadIt = threads.entrySet().iterator();
            while (threadIt.hasNext()) {
                Map.Entry entry = (Map.Entry)threadIt.next();
                if (entry.getValue().equals(Id)) {
                    threads.remove(entry.getKey());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void setAuto() {
        String tName = Thread.currentThread().getName();
        sessions.put(tName, "Auto");
    }

    public static boolean isAuto() {
        String tName = Thread.currentThread().getName();
        if (sessions.get(tName) instanceof String)
            return true;
        else
            return false;
    }

    public static Iterator iterator() {
        return sessions.values().iterator();
    }

    public static int numSessions() {
        return sessions.size();
    }

    public static String toXML() {
        StringBuffer xml = new StringBuffer();
        xml.append("<Admin>");
        Iterator it = sessions.values().iterator();
        while (it.hasNext()) {
            HttpSession sess = (HttpSession)it.next();
            StringBuffer sb = new StringBuffer();
            sb.append("<session>");
            try {
                XMLTag(sb,
                       "user",
                       (String)sess.getAttribute("edu.yale.its.tp.cas.client.filter.user"));
                XMLTag(sb,
                       "created",
                        DateFormat.getDateInstance(DateFormat.FULL).format(new Date(sess.getCreationTime())));
                XMLTag(sb,
                       "access",
                       DateFormat.getDateInstance(DateFormat.FULL).format(new Date(sess.getLastAccessedTime())));
                XMLTag(sb, "ip", (String)sess.getAttribute("IPAddress"));
                XMLTag(sb, "action", (String)sess.getAttribute("LastAction"));
                sb.append("</session>");
                xml.append(sb.toString());
            } catch (Exception e) {
                // SessionManager.removeSession(sess.getId());
            }
        }
        xml.append("</Admin>");
        return xml.toString();
    }

    public static void XMLTag(StringBuffer sb, String tag, Object obj) {
        if (obj != null) {
            StringTokenizer tokenizer;
            String token;
            String tokens = "<>&";
            tokenizer = new StringTokenizer(obj.toString(), tokens, true);
            sb.append("<").append(tag).append(">");
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                if (token.equals("<")) {
                    sb.append("&lt;");
                } else if (token.equals(">")) {
                    sb.append("&gt;");
                } else if (token.equals("&")) {
                    sb.append("&amp;");
                } else {
                    sb.append(token);
                }
            }
            sb.append("</").append(tag).append(">");
        } else {
            sb.append("<").append(tag).append(">");
            sb.append("null");
            sb.append("</").append(tag).append(">");
        }
    }

    public static void destroy() {
        Iterator sessIt = sessions.values().iterator();
        while (sessIt.hasNext()) {
            HttpSession sess = (HttpSession)sessIt.next();
            try {
                sess.invalidate();
            } catch (Exception e) {
            }
        }
        sessions = new Hashtable();
        threads = new HashMap();
    }

    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
       arg0.getSession().setAttribute("action", "Logged On");
        
    }

    public void sessionDestroyed(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        arg0.getSession().setAttribute("action","logout");
    }

    public void attributeAdded(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        if(arg0.getName().equals("action") || 
           arg0.getName().equals("edu.yale.its.tp.cas.client.filter.user") ||
           arg0.getName().equals("IPAddress")){
            writeLog(arg0.getSession(),(String)arg0.getSession().getAttribute("action"));
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    public void attributeReplaced(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
        if(arg0.getName().equals("action") || 
           arg0.getName().equals("edu.yale.its.tp.cas.client.filter.user") ||
           arg0.getName().equals("IPAddress")){
            writeLog(arg0.getSession(),(String)arg0.getSession().getAttribute("action"));
        }
    }
    
    public void writeLog(HttpSession session, String action){
        String user = "";
        String logOn = "";
        String ip = "";
        String lastAccess = "";
        try {
            user = (String)session.getAttribute("edu.yale.its.tp.cas.client.filter.user");
            if(user == null || user.equals(""))
                return;
            logOn = DateFormat.getDateTimeInstance().format(new Date(session.getCreationTime()));
            lastAccess = DateFormat.getDateTimeInstance().format(new Date());
            ip =  (String)session.getAttribute("IPAddress");

            ApplicationLogBuilder.sendJMSMessage(session.getId(),appName, user, logOn, ip, action, lastAccess);
        }catch(Exception e){
            //Try once more than stop.
            try {
                ApplicationLogBuilder.sendJMSMessage(session.getId(),appName, user, logOn, ip, action, lastAccess);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public static void init(String app){
        appName = app;
        try{
            ApplicationLogBuilder.sendJMSMessage("null",app,"null","null","null","initialize","null");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
