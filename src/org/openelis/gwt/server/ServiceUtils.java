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
package org.openelis.gwt.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpSession;
import javax.xml.transform.stream.StreamResult;

import org.openelis.util.SessionManager;
import org.openelis.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Set of static utility methods used on the Server side mostly related to XML  
 *
 */
public class ServiceUtils {

    public static String props;

    public static String getXML(String url) throws Exception {
        Document doc;

        try {
            doc = XMLUtil.createNew("doc");
            return getXML(url, doc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static String getGeneratorXML(InputStream xsl, String props, String language) throws Exception {
        Document doc;
        Element root, l;
        String locale = "en";
        ByteArrayOutputStream output;

        doc = XMLUtil.createNew("doc");
        root = doc.getDocumentElement();

        if ( !language.equals("default"))
            locale = language;
        l = doc.createElement("locale");
        l.appendChild(doc.createTextNode(locale));
        root.appendChild(l);

        l = doc.createElement("props");
        l.appendChild(doc.createTextNode(props));
        root.appendChild(l);

        output = new ByteArrayOutputStream();
        XMLUtil.transformXML(doc, xsl, new StreamResult(output));

        return new String(output.toByteArray(), "UTF-8");
    }

    public static String getXML(String url, Document doc) throws Exception {
        Element root, l;
        String locale = "en";
        ByteArrayOutputStream output;

        try {
            locale = getLocale();
            root = doc.getDocumentElement();
            l = doc.createElement("locale");
            l.appendChild(doc.createTextNode(locale));
            root.appendChild(l);

            l = doc.createElement("props");
            l.appendChild(doc.createTextNode(props));
            root.appendChild(l);
            
            output = new ByteArrayOutputStream();
            XMLUtil.transformXML(doc, new File(url), new StreamResult(output));
        
            return new String(output.toByteArray(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static String getXML(InputStream is, Document doc) throws Exception {
        Element root, l;
        String locale = "en";
        ByteArrayOutputStream output;

        try {
            locale = getLocale();
            root = doc.getDocumentElement();
            l = doc.createElement("locale");
            l.appendChild(doc.createTextNode(locale));
            root.appendChild(l);

            l = doc.createElement("props");
            l.appendChild(doc.createTextNode(props));
            root.appendChild(l);
            
            output = new ByteArrayOutputStream();
            XMLUtil.transformXML(doc, is, new StreamResult(output));
        
            return new String(output.toByteArray(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private static String getLocale() {
        String l;
        HttpSession s;
        
        l = null;
        s = SessionManager.getSession();
        if (s != null)
            l = (String) s.getAttribute("locale");
        if (l == null)
            l = "en";
        
        return l;
    }
}
