package org.openelis.gwt.server;

import org.openelis.gwt.common.RPCException;
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

    public static HashMap getPermissions() throws RPCException {
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
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            XMLUtil.transformXML(doc, new File(url), new StreamResult(bytes));
            return bytes.toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new RPCException(e.getMessage());
        }
    }

}
