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
package org.openelis.gwt.server;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.SecurityUtil;
import org.openelis.util.SessionManager;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.xml.transform.stream.StreamResult;

public class ServiceUtils {
    
    public static String props;
    
    public static SecurityUtil getSecurity() {
        return (SecurityUtil)SessionManager.getSession().getAttribute("security");
    }
    
    public static boolean hasPermission(String module) {
        try {
            boolean result = true;
            Boolean access = (Boolean)getPermissions().get(module);
            if (access != null) {
                return access.booleanValue();
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static HashMap<String,Boolean> getPermissions() throws RPCException {
        // TODO Auto-generated method stub
        try {
            HttpSession sess = SessionManager.getSession();
            HashMap perms = null;
            if (sess.getAttribute("permissions") != null) {
                perms = (HashMap)sess.getAttribute("permissions");
            } else {
                sess.setAttribute("permissions", perms);
            }
            return perms;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }
    
    public static String getXML(String url) throws RPCException {
        try{
            Document doc = XMLUtil.createNew("doc");
            return getXML(url,doc);
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }

    }
    
    public static String getXML(String url, Document doc) throws RPCException {
        try {
            String loc = "en";
            if(SessionManager.getSession().getAttribute("locale") != null)
                loc = (String)SessionManager.getSession().getAttribute("locale");
            Element root = doc.getDocumentElement();
            Element locale = doc.createElement("locale");
            locale.appendChild(doc.createTextNode(loc));
            root.appendChild(locale);
            Element propsEL = doc.createElement("props");
            propsEL.appendChild(doc.createTextNode(props));
            root.appendChild(propsEL);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            XMLUtil.transformXML(doc, new File(url), new StreamResult(bytes));
            return bytes.toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }

}
